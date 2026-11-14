package com.example.chitieu.ui.report

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chitieu.data.local.entity.CategoryTotal
import com.example.chitieu.data.local.entity.HomeSummary
import com.example.chitieu.data.local.entity.TransactionType
import com.example.chitieu.data.repository.TransactionRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn

@OptIn(ExperimentalCoroutinesApi::class)
class ReportViewModel : ViewModel() {
    private val transactionRepo = TransactionRepository.getInstance()

    private val _selectedType = MutableStateFlow(TransactionType.EXPENSE)
    val selectedType: StateFlow<TransactionType> = _selectedType

    val summary: StateFlow<HomeSummary> = transactionRepo.getHomeSummary()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), HomeSummary(0.0, 0.0, 0.0, emptyList()))

    val categoryTotals: StateFlow<List<CategoryTotal>> = _selectedType
        .flatMapLatest { type ->
            transactionRepo.getCategoryTotals(type)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun setSelectedType(type: TransactionType) {
        _selectedType.value = type
    }
}
