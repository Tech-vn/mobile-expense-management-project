package com.example.chitieu.data.local.entity

data class BudgetEntity(
    val id: String,
    val categoryId: String?, // null for overall total budget limit
    val limitAmount: Double,
    val monthYear: String // e.g. "03/2025"
)
