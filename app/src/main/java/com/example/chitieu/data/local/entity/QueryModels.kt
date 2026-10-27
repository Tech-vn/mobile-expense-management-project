package com.example.chitieu.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation

/**
 * Kết hợp một Giao dịch với thông tin Chi tiết của Danh mục tương ứng.
 */
data class TransactionWithCategory(
    @Embedded val transaction: TransactionEntity,
    @Relation(
        parentColumn = "categoryId",
        entityColumn = "id"
    )
    val category: CategoryEntity
)

/**
 * Tổng tiền giao dịch chi tiêu theo từng danh mục trong khoảng thời gian.
 */
data class CategorySum(
    val categoryId: Long,
    val categoryName: String,
    val categoryIcon: String,
    val categoryColor: String,
    val totalAmount: Long
)

/**
 * Tổng quan thu chi trong tháng (hoặc khoảng thời gian bất kỳ).
 */
data class MonthlySummary(
    val totalIncome: Long = 0L,
    val totalExpense: Long = 0L
)

/**
 * Chi tiết ngân sách kèm theo số tiền đã chi thực tế trong tháng.
 */
data class BudgetWithUsage(
    val budgetId: Long,
    val categoryId: Long,
    val categoryName: String,
    val categoryIcon: String,
    val categoryColor: String,
    val amountLimit: Long,
    val spentAmount: Long,
    val month: Int,
    val year: Int
)
