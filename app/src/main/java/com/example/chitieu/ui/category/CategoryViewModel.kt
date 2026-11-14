package com.example.chitieu.ui.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chitieu.data.local.entity.CategoryEntity
import com.example.chitieu.data.local.entity.TransactionType
import com.example.chitieu.data.repository.CategoryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class CategoryViewModel : ViewModel() {
    private val categoryRepo = CategoryRepository.getInstance()

    private val _selectedType = MutableStateFlow(TransactionType.EXPENSE)
    val selectedType: StateFlow<TransactionType> = _selectedType

    val categories: StateFlow<List<CategoryEntity>> = combine(
        categoryRepo.categories,
        _selectedType
    ) { list, type ->
        list.filter { it.type == type }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun setSelectedType(type: TransactionType) {
        _selectedType.value = type
    }

    fun addCategory(name: String, iconName: String, colorHex: String) {
        categoryRepo.addCategory(name, _selectedType.value, iconName, colorHex)
    }

    fun deleteCategory(id: String) {
        categoryRepo.deleteCategory(id)
    }
}
