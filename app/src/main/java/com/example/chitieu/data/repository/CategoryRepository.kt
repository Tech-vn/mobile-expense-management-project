package com.example.chitieu.data.repository

import com.example.chitieu.data.local.dao.BudgetDao
import com.example.chitieu.data.local.dao.CategoryDao
import com.example.chitieu.data.local.entity.CategoryEntity
import com.example.chitieu.data.local.entity.TransactionType
import com.example.chitieu.di.AppModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.UUID

class CategoryRepository(
    private val categoryDao: CategoryDao,
    private val budgetDao: BudgetDao,
    private val scope: CoroutineScope
) {
    val categories: Flow<List<CategoryEntity>> = categoryDao.observeAll()

    fun getCategoriesByType(type: TransactionType): Flow<List<CategoryEntity>> {
        return categories.map { list -> list.filter { it.type == type } }
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
        scope.launch { categoryDao.insert(newCat) }
    }

    fun updateCategory(updated: CategoryEntity) {
        scope.launch { categoryDao.update(updated) }
    }

    fun deleteCategory(id: String) {
        scope.launch {
            categoryDao.deleteById(id)
            budgetDao.deleteByCategoryId(id) // ngân sách của danh mục này không còn ý nghĩa
        }
    }

    companion object {
        fun getInstance(): CategoryRepository = AppModule.categoryRepository
    }
}
