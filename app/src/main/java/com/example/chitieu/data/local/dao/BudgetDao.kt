package com.example.chitieu.data.local.dao

import androidx.room.*
import com.example.chitieu.data.local.entity.BudgetEntity
import com.example.chitieu.data.local.entity.BudgetWithUsage
import kotlinx.coroutines.flow.Flow

@Dao
interface BudgetDao {
    @Query("""
        SELECT
            b.id AS budgetId,
            c.id AS categoryId,
            c.name AS categoryName,
            c.icon AS categoryIcon,
            c.color AS categoryColor,
            b.amountLimit AS amountLimit,
            COALESCE(SUM(t.amount), 0) AS spentAmount,
            b.month AS month,
            b.year AS year
        FROM budgets b
        INNER JOIN categories c ON b.categoryId = c.id
        LEFT JOIN transactions t ON t.categoryId = c.id
            AND t.type = 'EXPENSE'
            AND t.date >= :startDate
            AND t.date <= :endDate
        WHERE b.month = :month AND b.year = :year
        GROUP BY b.id
    """)
    fun getBudgetsWithUsage(month: Int, year: Int, startDate: Long, endDate: Long): Flow<List<BudgetWithUsage>>

    @Query("SELECT * FROM budgets WHERE categoryId = :categoryId AND month = :month AND year = :year LIMIT 1")
    suspend fun getBudgetByCategoryAndMonth(categoryId: Long, month: Int, year: Int): BudgetEntity?

    @Query("SELECT * FROM budgets WHERE month = :month AND year = :year")
    fun getBudgetsByMonth(month: Int, year: Int): Flow<List<BudgetEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateBudget(budget: BudgetEntity): Long

    @Update
    suspend fun updateBudget(budget: BudgetEntity)

    @Delete
    suspend fun deleteBudget(budget: BudgetEntity)

    @Query("DELETE FROM budgets WHERE id = :id")
    suspend fun deleteBudgetById(id: Long)
}
