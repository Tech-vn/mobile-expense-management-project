package com.example.chitieu.data.local.entity

data class CategoryEntity(
    val id: String,
    val name: String,
    val type: TransactionType,
    val iconName: String,
    val colorHex: String,
    val isDefault: Boolean = false
)
