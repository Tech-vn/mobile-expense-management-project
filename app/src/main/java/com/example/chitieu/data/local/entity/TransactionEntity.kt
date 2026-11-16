package com.example.chitieu.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

// Không đặt khóa ngoại tới categories: xóa một danh mục thì lịch sử giao dịch vẫn còn,
// tên danh mục được lưu sẵn trong categoryName (đúng cách UI đang hoạt động).
@Entity(
    tableName = "transactions",
    indices = [Index("categoryId"), Index("dateMillis")]
)
data class TransactionEntity(
    @PrimaryKey val id: String,
    val title: String,
    val amount: Double,
    val type: TransactionType,
    val categoryId: String,
    val categoryName: String,
    val dateMillis: Long,
    val note: String = ""
)
