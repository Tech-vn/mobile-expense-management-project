package com.example.chitieu.data.local.entity

data class TransactionEntity(
    val id: String,
    val title: String,
    val amount: Double,
    val type: TransactionType,
    val categoryId: String,
    val categoryName: String,
    val dateMillis: Long,
    val note: String = ""
)
