package com.example.chitieu.ui.budget

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chitieu.data.local.entity.BudgetStatus
import com.example.chitieu.data.local.entity.CategoryEntity
import com.example.chitieu.data.local.entity.TransactionType
import com.example.chitieu.data.repository.BudgetRepository
import com.example.chitieu.data.repository.CategoryRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class BudgetViewModel : ViewModel() {
    private val budgetRepo = BudgetRepository.getInstance()
    private val categoryRepo = CategoryRepository.getInstance()

    val budgetStatuses: StateFlow<List<BudgetStatus>> = budgetRepo.getBudgetStatuses()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val expenseCategories: StateFlow<List<CategoryEntity>> = categoryRepo.getCategoriesByType(TransactionType.EXPENSE)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun saveBudget(categoryId: String?, limitAmount: Double) {
        budgetRepo.saveBudget(categoryId, limitAmount)
    }

    fun deleteBudget(id: String) {
        budgetRepo.deleteBudget(id)
    }
}
