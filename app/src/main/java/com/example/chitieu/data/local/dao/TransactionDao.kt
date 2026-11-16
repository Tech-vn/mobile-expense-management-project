package com.example.chitieu.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.chitieu.data.local.entity.CategoryTotalRow
import com.example.chitieu.data.local.entity.TransactionEntity
import com.example.chitieu.data.local.entity.TransactionType
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Query("SELECT * FROM transactions ORDER BY dateMillis DESC")
    fun observeAll(): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions ORDER BY dateMillis DESC")
    suspend fun getAll(): List<TransactionEntity>

    @Query("SELECT * FROM transactions ORDER BY dateMillis DESC LIMIT :limit")
    fun observeRecent(limit: Int): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE type = :type")
    fun observeByType(type: TransactionType): Flow<List<TransactionEntity>>

    @Query("SELECT COALESCE(SUM(amount), 0) FROM transactions WHERE type = :type")
    fun observeTotal(type: TransactionType): Flow<Double>

    @Query(
        "SELECT t.categoryId AS categoryId, " +
            "COALESCE(c.name, MAX(t.categoryName)) AS categoryName, " +
            "COALESCE(c.iconName, 'AttachMoney') AS iconName, " +
            "COALESCE(c.colorHex, '#9E9E9E') AS colorHex, " +
            "SUM(t.amount) AS totalAmount " +
            "FROM transactions t " +
            "LEFT JOIN categories c ON c.id = t.categoryId " +
            "WHERE t.type = :type " +
            "GROUP BY t.categoryId " +
            "ORDER BY totalAmount DESC"
    )
    fun observeCategoryTotals(type: TransactionType): Flow<List<CategoryTotalRow>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(transaction: TransactionEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(transactions: List<TransactionEntity>)

    @Update
    suspend fun update(transaction: TransactionEntity)

    @Query("DELETE FROM transactions WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("DELETE FROM transactions")
    suspend fun deleteAll()
}
