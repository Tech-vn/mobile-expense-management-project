package com.example.chitieu.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.chitieu.data.local.entity.BudgetEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BudgetDao {
    @Query("SELECT * FROM budgets WHERE monthYear = :monthYear ORDER BY rowid")
    fun observeByMonth(monthYear: String): Flow<List<BudgetEntity>>

    @Query("SELECT * FROM budgets WHERE categoryId IS :categoryId AND monthYear = :monthYear LIMIT 1")
    suspend fun find(categoryId: String?, monthYear: String): BudgetEntity?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(budget: BudgetEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(budgets: List<BudgetEntity>)

    @Query("UPDATE budgets SET limitAmount = :limitAmount WHERE id = :id")
    suspend fun updateLimit(id: String, limitAmount: Double)

    @Query("DELETE FROM budgets WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("DELETE FROM budgets WHERE categoryId = :categoryId")
    suspend fun deleteByCategoryId(categoryId: String)

    @Query("DELETE FROM budgets")
    suspend fun deleteAll()
}
