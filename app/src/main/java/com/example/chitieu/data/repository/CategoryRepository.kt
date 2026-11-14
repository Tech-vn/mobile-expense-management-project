package com.example.chitieu.data.repository

import com.example.chitieu.data.local.DatabaseSeeder
import com.example.chitieu.data.local.entity.CategoryEntity
import com.example.chitieu.data.local.entity.TransactionType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import java.util.UUID

class CategoryRepository private constructor() {
    private val _categories = MutableStateFlow(DatabaseSeeder.defaultCategories)
    val categories: Flow<List<CategoryEntity>> = _categories.asStateFlow()

    fun getCategoriesByType(type: TransactionType): Flow<List<CategoryEntity>> {
        return _categories.map { list -> list.filter { it.type == type } }
    }

    fun addCategory(name: String, type: TransactionType, iconName: String, colorHex: String) {
        val newCat = CategoryEntity(
            id = "cat_" + UUID.randomUUID().toString().take(8),
            name = name,
            type = type,
            iconName = iconName,
            colorHex = colorHex,
            isDefault = false
        )
        _categories.value = _categories.value + newCat
    }

    fun updateCategory(updated: CategoryEntity) {
        _categories.value = _categories.value.map {
            if (it.id == updated.id) updated else it
        }
    }

    fun deleteCategory(id: String) {
        _categories.value = _categories.value.filter { it.id != id }
    }

    fun resetToDefaultData() {
        _categories.value = DatabaseSeeder.defaultCategories
    }

    companion object {
        @Volatile
        private var INSTANCE: CategoryRepository? = null

        fun getInstance(): CategoryRepository {
            return INSTANCE ?: synchronized(this) {
                val instance = CategoryRepository()
                INSTANCE = instance
                instance
            }
        }
    }
}
