package com.example.chitieu.data.repository

import com.example.chitieu.data.local.DatabaseSeeder
import com.example.chitieu.data.local.entity.CategoryTotal
import com.example.chitieu.data.local.entity.HomeSummary
import com.example.chitieu.data.local.entity.TransactionEntity
import com.example.chitieu.data.local.entity.TransactionType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import java.util.UUID

class TransactionRepository private constructor() {
    private val _transactions = MutableStateFlow(DatabaseSeeder.getSampleTransactions())
    val transactions: Flow<List<TransactionEntity>> = _transactions.asStateFlow()

    fun getTransactionList(): List<TransactionEntity> = _transactions.value

    fun addTransaction(
        title: String,
        amount: Double,
        type: TransactionType,
        categoryId: String,
        categoryName: String,
        dateMillis: Long,
        note: String
    ) {
        val newTx = TransactionEntity(
            id = "tx_" + UUID.randomUUID().toString().take(8),
            title = title,
            amount = amount,
            type = type,
            categoryId = categoryId,
            categoryName = categoryName,
            dateMillis = dateMillis,
            note = note
        )
        _transactions.value = listOf(newTx) + _transactions.value
    }

    fun updateTransaction(updated: TransactionEntity) {
        _transactions.value = _transactions.value.map {
            if (it.id == updated.id) updated else it
        }
    }

    fun deleteTransaction(id: String) {
        _transactions.value = _transactions.value.filter { it.id != id }
    }

    fun resetToDefaultData() {
        _transactions.value = DatabaseSeeder.getSampleTransactions()
    }

    fun getHomeSummary(): Flow<HomeSummary> {
        return _transactions.map { list ->
            val totalIncome = list.filter { it.type == TransactionType.INCOME }.sumOf { it.amount }
            val totalExpense = list.filter { it.type == TransactionType.EXPENSE }.sumOf { it.amount }
            val totalBalance = totalIncome - totalExpense
            val recent = list.take(5)
            HomeSummary(
                totalBalance = totalBalance,
                totalIncome = totalIncome,
                totalExpense = totalExpense,
                recentTransactions = recent
            )
        }
    }

    fun getCategoryTotals(type: TransactionType): Flow<List<CategoryTotal>> {
        return _transactions.map { list ->
            val filtered = list.filter { it.type == type }
            val totalSum = filtered.sumOf { it.amount }
            if (totalSum == 0.0) return@map emptyList()

            val categoryMap = DatabaseSeeder.defaultCategories.associateBy { it.id }

            filtered.groupBy { it.categoryId }
                .map { (catId, txList) ->
                    val catSum = txList.sumOf { it.amount }
                    val catObj = categoryMap[catId]
                    val catName = catObj?.name ?: txList.firstOrNull()?.categoryName ?: "Khác"
                    val iconName = catObj?.iconName ?: "AttachMoney"
                    val colorHex = catObj?.colorHex ?: "#9E9E9E"
                    val percentage = (catSum / totalSum * 100).toFloat()

                    CategoryTotal(
                        categoryId = catId,
                        categoryName = catName,
                        iconName = iconName,
                        colorHex = colorHex,
                        totalAmount = catSum,
                        percentage = percentage
                    )
                }
                .sortedByDescending { it.totalAmount }
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: TransactionRepository? = null

        fun getInstance(): TransactionRepository {
            return INSTANCE ?: synchronized(this) {
                val instance = TransactionRepository()
                INSTANCE = instance
                instance
            }
        }
    }
}
