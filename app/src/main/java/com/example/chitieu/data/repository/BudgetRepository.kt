package com.example.chitieu.data.repository

import com.example.chitieu.data.local.DatabaseSeeder
import com.example.chitieu.data.local.entity.BudgetEntity
import com.example.chitieu.data.local.entity.BudgetStatus
import com.example.chitieu.data.local.entity.TransactionType
import com.example.chitieu.util.DateUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import java.util.UUID

class BudgetRepository private constructor() {
    private val _budgets = MutableStateFlow(DatabaseSeeder.getSampleBudgets())
    val budgets: Flow<List<BudgetEntity>> = _budgets.asStateFlow()

    private val transactionRepo = TransactionRepository.getInstance()

    fun getBudgetStatuses(): Flow<List<BudgetStatus>> {
        val currentMonthYear = DateUtils.getCurrentMonthYear()

        return combine(_budgets, transactionRepo.transactions) { budgetList, txList ->
            val monthTx = txList.filter {
                it.type == TransactionType.EXPENSE && DateUtils.getMonthYear(it.dateMillis) == currentMonthYear
            }

            val categoryMap = DatabaseSeeder.defaultCategories.associateBy { it.id }

            budgetList.map { budget ->
                val spentAmount = if (budget.categoryId == null) {
                    // Overall budget
                    monthTx.sumOf { it.amount }
                } else {
                    monthTx.filter { it.categoryId == budget.categoryId }.sumOf { it.amount }
                }

                val categoryName = if (budget.categoryId == null) {
                    "Tổng ngân sách tháng"
                } else {
                    categoryMap[budget.categoryId]?.name ?: "Danh mục"
                }

                val remaining = budget.limitAmount - spentAmount
                val progress = if (budget.limitAmount > 0) (spentAmount / budget.limitAmount).toFloat() else 0f

                BudgetStatus(
                    budget = budget,
                    categoryName = categoryName,
                    spentAmount = spentAmount,
                    remainingAmount = remaining,
                    progress = progress
                )
            }
        }
    }

    fun saveBudget(categoryId: String?, limitAmount: Double) {
        val currentMonthYear = DateUtils.getCurrentMonthYear()
        val existing = _budgets.value.find { it.categoryId == categoryId && it.monthYear == currentMonthYear }

        if (existing != null) {
            _budgets.value = _budgets.value.map {
                if (it.id == existing.id) it.copy(limitAmount = limitAmount) else it
            }
        } else {
            val newBudget = BudgetEntity(
                id = "b_" + UUID.randomUUID().toString().take(8),
                categoryId = categoryId,
                limitAmount = limitAmount,
                monthYear = currentMonthYear
            )
            _budgets.value = _budgets.value + newBudget
        }
    }

    fun deleteBudget(id: String) {
        _budgets.value = _budgets.value.filter { it.id != id }
    }

    fun resetToDefaultData() {
        _budgets.value = DatabaseSeeder.getSampleBudgets()
    }

    companion object {
        @Volatile
        private var INSTANCE: BudgetRepository? = null

        fun getInstance(): BudgetRepository {
            return INSTANCE ?: synchronized(this) {
                val instance = BudgetRepository()
                INSTANCE = instance
                instance
            }
        }
    }
}
