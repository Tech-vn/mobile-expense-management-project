package com.example.chitieu.data.local

import com.example.chitieu.data.local.dao.CategoryDao
import com.example.chitieu.data.local.entity.CategoryEntity
import com.example.chitieu.data.local.entity.TransactionType

object DatabaseSeeder {
    val defaultCategories = listOf(
        CategoryEntity(name = "Ăn uống", icon = "restaurant", color = "#FF5722", type = TransactionType.EXPENSE, isDefault = true),
        CategoryEntity(name = "Mua sắm", icon = "shopping_bag", color = "#E91E63", type = TransactionType.EXPENSE, isDefault = true),
        CategoryEntity(name = "Di chuyển", icon = "directions_bus", color = "#2196F3", type = TransactionType.EXPENSE, isDefault = true),
        CategoryEntity(name = "Giải trí", icon = "movie", color = "#9C27B0", type = TransactionType.EXPENSE, isDefault = true),
        CategoryEntity(name = "Hóa đơn & Tiện ích", icon = "receipt_long", color = "#FF9800", type = TransactionType.EXPENSE, isDefault = true),
        CategoryEntity(name = "Lương", icon = "payments", color = "#4CAF50", type = TransactionType.INCOME, isDefault = true),
        CategoryEntity(name = "Thưởng", icon = "card_giftcard", color = "#00BCD4", type = TransactionType.INCOME, isDefault = true),
        CategoryEntity(name = "Đầu tư", icon = "trending_up", color = "#3F51B5", type = TransactionType.INCOME, isDefault = true)
    )

    suspend fun seedDefaultCategories(categoryDao: CategoryDao) {
        categoryDao.insertCategories(defaultCategories)
    }
}
