package com.example.chitieu.data.repository

import com.example.chitieu.data.local.dao.TransactionDao
import com.example.chitieu.data.local.entity.CategoryTotal
import com.example.chitieu.data.local.entity.HomeSummary
import com.example.chitieu.data.local.entity.TransactionEntity
import com.example.chitieu.data.local.entity.TransactionType
import com.example.chitieu.di.AppModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.UUID

class TransactionRepository(
    private val dao: TransactionDao,
    private val scope: CoroutineScope
) {
    val transactions: Flow<List<TransactionEntity>> = dao.observeAll()

    suspend fun getTransactionList(): List<TransactionEntity> = dao.getAll()

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
        scope.launch { dao.insert(newTx) }
    }

    fun updateTransaction(updated: TransactionEntity) {
        scope.launch { dao.update(updated) }
    }

    fun deleteTransaction(id: String) {
        scope.launch { dao.deleteById(id) }
    }

    fun getHomeSummary(): Flow<HomeSummary> {
        return combine(
            dao.observeTotal(TransactionType.INCOME),
            dao.observeTotal(TransactionType.EXPENSE),
            dao.observeRecent(5)
        ) { income, expense, recent ->
            HomeSummary(
                totalBalance = income - expense,
                totalIncome = income,
                totalExpense = expense,
                recentTransactions = recent
            )
        }
    }

    fun getCategoryTotals(type: TransactionType): Flow<List<CategoryTotal>> {
        return dao.observeCategoryTotals(type).map { rows ->
            val totalSum = rows.sumOf { it.totalAmount }
            if (totalSum == 0.0) {
                emptyList()
            } else {
                rows.map { row ->
                    CategoryTotal(
                        categoryId = row.categoryId,
                        categoryName = row.categoryName,
                        iconName = row.iconName,
                        colorHex = row.colorHex,
                        totalAmount = row.totalAmount,
                        percentage = (row.totalAmount / totalSum * 100).toFloat()
                    )
                }
            }
        }
    }

    companion object {
        fun getInstance(): TransactionRepository = AppModule.transactionRepository
    }
}
