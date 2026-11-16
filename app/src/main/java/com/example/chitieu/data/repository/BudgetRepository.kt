package com.example.chitieu.data.repository

import com.example.chitieu.data.local.dao.BudgetDao
import com.example.chitieu.data.local.dao.CategoryDao
import com.example.chitieu.data.local.dao.TransactionDao
import com.example.chitieu.data.local.entity.BudgetEntity
import com.example.chitieu.data.local.entity.BudgetStatus
import com.example.chitieu.data.local.entity.TransactionType
import com.example.chitieu.di.AppModule
import com.example.chitieu.util.DateUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.util.UUID

class BudgetRepository(
    private val budgetDao: BudgetDao,
    private val categoryDao: CategoryDao,
    private val transactionDao: TransactionDao,
    private val scope: CoroutineScope
) {
    val budgets: Flow<List<BudgetEntity>>
        get() = budgetDao.observeByMonth(DateUtils.getCurrentMonthYear())

    fun getBudgetStatuses(): Flow<List<BudgetStatus>> {
        val currentMonthYear = DateUtils.getCurrentMonthYear()

        return combine(
            budgetDao.observeByMonth(currentMonthYear),
            categoryDao.observeAll(),
            transactionDao.observeByType(TransactionType.EXPENSE)
        ) { budgetList, categories, expenses ->
            val monthTx = expenses.filter { DateUtils.getMonthYear(it.dateMillis) == currentMonthYear }
            val categoryMap = categories.associateBy { it.id }

            budgetList.map { budget ->
                val spentAmount = if (budget.categoryId == null) {
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
        scope.launch {
            val existing = budgetDao.find(categoryId, currentMonthYear)
            if (existing != null) {
                budgetDao.updateLimit(existing.id, limitAmount)
            } else {
                budgetDao.insert(
                    BudgetEntity(
                        id = "b_" + UUID.randomUUID().toString().take(8),
                        categoryId = categoryId,
                        limitAmount = limitAmount,
                        monthYear = currentMonthYear
                    )
                )
            }
        }
    }

    fun deleteBudget(id: String) {
        scope.launch { budgetDao.deleteById(id) }
    }

    companion object {
        fun getInstance(): BudgetRepository = AppModule.budgetRepository
    }
}
