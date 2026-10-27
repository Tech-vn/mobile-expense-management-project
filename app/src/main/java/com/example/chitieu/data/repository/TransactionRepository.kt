package com.example.chitieu.data.repository

import com.example.chitieu.data.local.dao.TransactionDao
import com.example.chitieu.data.local.entity.CategorySum
import com.example.chitieu.data.local.entity.MonthlySummary
import com.example.chitieu.data.local.entity.TransactionEntity
import com.example.chitieu.data.local.entity.TransactionWithCategory
import kotlinx.coroutines.flow.Flow

class TransactionRepository(private val transactionDao: TransactionDao) {

    fun getAllTransactionsWithCategory(): Flow<List<TransactionWithCategory>> {
        return transactionDao.getAllTransactionsWithCategory()
    }

    fun getTransactionsByDateRange(startDate: Long, endDate: Long): Flow<List<TransactionWithCategory>> {
        return transactionDao.getTransactionsByDateRange(startDate, endDate)
    }

    fun getTransactionsByCategory(categoryId: Long): Flow<List<TransactionWithCategory>> {
        return transactionDao.getTransactionsByCategory(categoryId)
    }

    fun getTransactionsByDateRangeAndCategory(startDate: Long, endDate: Long, categoryId: Long): Flow<List<TransactionWithCategory>> {
        return transactionDao.getTransactionsByDateRangeAndCategory(startDate, endDate, categoryId)
    }

    suspend fun getTransactionById(id: Long): TransactionWithCategory? {
        return transactionDao.getTransactionById(id)
    }

    fun getMonthlySummary(startDate: Long, endDate: Long): Flow<MonthlySummary> {
        return transactionDao.getMonthlySummary(startDate, endDate)
    }

    fun getExpenseSumByCategory(startDate: Long, endDate: Long): Flow<List<CategorySum>> {
        return transactionDao.getExpenseSumByCategory(startDate, endDate)
    }

    suspend fun insertTransaction(transaction: TransactionEntity): Long {
        return transactionDao.insertTransaction(transaction)
    }

    suspend fun updateTransaction(transaction: TransactionEntity) {
        transactionDao.updateTransaction(transaction)
    }

    suspend fun deleteTransaction(transaction: TransactionEntity) {
        transactionDao.deleteTransaction(transaction)
    }

    suspend fun deleteTransactionById(id: Long) {
        transactionDao.deleteTransactionById(id)
    }
}
