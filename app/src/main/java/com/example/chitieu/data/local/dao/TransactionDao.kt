package com.example.chitieu.data.local.dao

import androidx.room.*
import com.example.chitieu.data.local.entity.CategorySum
import com.example.chitieu.data.local.entity.MonthlySummary
import com.example.chitieu.data.local.entity.TransactionEntity
import com.example.chitieu.data.local.entity.TransactionWithCategory
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Transaction
    @Query("SELECT * FROM transactions ORDER BY date DESC")
    fun getAllTransactionsWithCategory(): Flow<List<TransactionWithCategory>>

    @Transaction
    @Query("SELECT * FROM transactions WHERE date >= :startDate AND date <= :endDate ORDER BY date DESC")
    fun getTransactionsByDateRange(startDate: Long, endDate: Long): Flow<List<TransactionWithCategory>>

    @Transaction
    @Query("SELECT * FROM transactions WHERE categoryId = :categoryId ORDER BY date DESC")
    fun getTransactionsByCategory(categoryId: Long): Flow<List<TransactionWithCategory>>

    @Transaction
    @Query("SELECT * FROM transactions WHERE categoryId = :categoryId AND date >= :startDate AND date <= :endDate ORDER BY date DESC")
    fun getTransactionsByDateRangeAndCategory(startDate: Long, endDate: Long, categoryId: Long): Flow<List<TransactionWithCategory>>

    @Transaction
    @Query("SELECT * FROM transactions WHERE id = :id")
    suspend fun getTransactionById(id: Long): TransactionWithCategory?

    @Query("""
        SELECT
            COALESCE(SUM(CASE WHEN type = 'INCOME' THEN amount ELSE 0 END), 0) AS totalIncome,
            COALESCE(SUM(CASE WHEN type = 'EXPENSE' THEN amount ELSE 0 END), 0) AS totalExpense
        FROM transactions
        WHERE date >= :startDate AND date <= :endDate
    """)
    fun getMonthlySummary(startDate: Long, endDate: Long): Flow<MonthlySummary>

    @Query("""
        SELECT
            c.id AS categoryId,
            c.name AS categoryName,
            c.icon AS categoryIcon,
            c.color AS categoryColor,
            COALESCE(SUM(t.amount), 0) AS totalAmount
        FROM categories c
        INNER JOIN transactions t ON c.id = t.categoryId
        WHERE t.type = 'EXPENSE' AND t.date >= :startDate AND t.date <= :endDate
        GROUP BY c.id
        ORDER BY totalAmount DESC
    """)
    fun getExpenseSumByCategory(startDate: Long, endDate: Long): Flow<List<CategorySum>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: TransactionEntity): Long

    @Update
    suspend fun updateTransaction(transaction: TransactionEntity)

    @Delete
    suspend fun deleteTransaction(transaction: TransactionEntity)

    @Query("DELETE FROM transactions WHERE id = :id")
    suspend fun deleteTransactionById(id: Long)
}
