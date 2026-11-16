package com.example.chitieu.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "budgets",
    indices = [Index("categoryId"), Index("monthYear")]
)
data class BudgetEntity(
    @PrimaryKey val id: String,
    val categoryId: String?, // null = ngân sách tổng của tháng
    val limitAmount: Double,
    val monthYear: String // ví dụ "11/2026"
)
