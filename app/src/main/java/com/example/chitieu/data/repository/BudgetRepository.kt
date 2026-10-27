package com.example.chitieu.data.repository

import com.example.chitieu.data.local.dao.BudgetDao
import com.example.chitieu.data.local.entity.BudgetEntity
import com.example.chitieu.data.local.entity.BudgetWithUsage
import kotlinx.coroutines.flow.Flow

class BudgetRepository(private val budgetDao: BudgetDao) {

    fun getBudgetsWithUsage(month: Int, year: Int, startDate: Long, endDate: Long): Flow<List<BudgetWithUsage>> {
        return budgetDao.getBudgetsWithUsage(month, year, startDate, endDate)
    }

    suspend fun getBudgetByCategoryAndMonth(categoryId: Long, month: Int, year: Int): BudgetEntity? {
        return budgetDao.getBudgetByCategoryAndMonth(categoryId, month, year)
    }

    fun getBudgetsByMonth(month: Int, year: Int): Flow<List<BudgetEntity>> {
        return budgetDao.getBudgetsByMonth(month, year)
    }

    suspend fun insertOrUpdateBudget(budget: BudgetEntity): Long {
        return budgetDao.insertOrUpdateBudget(budget)
    }

    suspend fun updateBudget(budget: BudgetEntity) {
        budgetDao.updateBudget(budget)
    }

    suspend fun deleteBudget(budget: BudgetEntity) {
        budgetDao.deleteBudget(budget)
    }

    suspend fun deleteBudgetById(id: Long) {
        budgetDao.deleteBudgetById(id)
    }
}
