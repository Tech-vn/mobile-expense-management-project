package com.example.chitieu.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chitieu.data.local.entity.BudgetStatus
import com.example.chitieu.data.local.entity.CategoryEntity
import com.example.chitieu.data.local.entity.HomeSummary
import com.example.chitieu.data.repository.BudgetRepository
import com.example.chitieu.data.repository.CategoryRepository
import com.example.chitieu.data.repository.TransactionRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class HomeViewModel : ViewModel() {
    private val transactionRepo = TransactionRepository.getInstance()
    private val budgetRepo = BudgetRepository.getInstance()
    private val categoryRepo = CategoryRepository.getInstance()

    val summary: StateFlow<HomeSummary> = transactionRepo.getHomeSummary()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = HomeSummary(0.0, 0.0, 0.0, emptyList())
        )

    val budgetStatuses: StateFlow<List<BudgetStatus>> = budgetRepo.getBudgetStatuses()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val categories: StateFlow<List<CategoryEntity>> = categoryRepo.categories
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun deleteTransaction(id: String) {
        transactionRepo.deleteTransaction(id)
    }
}
