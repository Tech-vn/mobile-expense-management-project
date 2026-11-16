package com.example.chitieu.data.local.entity

// Kết quả Room trả về từ câu query JOIN + GROUP BY (chưa có phần trăm)
data class CategoryTotalRow(
    val categoryId: String,
    val categoryName: String,
    val iconName: String,
    val colorHex: String,
    val totalAmount: Double
)

data class CategoryTotal(
    val categoryId: String,
    val categoryName: String,
    val iconName: String,
    val colorHex: String,
    val totalAmount: Double,
    val percentage: Float
)

data class BudgetStatus(
    val budget: BudgetEntity,
    val categoryName: String,
    val spentAmount: Double,
    val remainingAmount: Double,
    val progress: Float // 0.0f to 1.0f+
)

data class HomeSummary(
    val totalBalance: Double,
    val totalIncome: Double,
    val totalExpense: Double,
    val recentTransactions: List<TransactionEntity>
)
