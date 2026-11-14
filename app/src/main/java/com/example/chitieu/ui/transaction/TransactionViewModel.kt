package com.example.chitieu.ui.transaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chitieu.data.local.entity.CategoryEntity
import com.example.chitieu.data.local.entity.TransactionEntity
import com.example.chitieu.data.local.entity.TransactionType
import com.example.chitieu.data.repository.CategoryRepository
import com.example.chitieu.data.repository.TransactionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

enum class TransactionFilter {
    ALL,
    EXPENSE,
    INCOME
}

class TransactionViewModel : ViewModel() {
    private val transactionRepo = TransactionRepository.getInstance()
    private val categoryRepo = CategoryRepository.getInstance()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _selectedFilter = MutableStateFlow(TransactionFilter.ALL)
    val selectedFilter: StateFlow<TransactionFilter> = _selectedFilter

    val categories: StateFlow<List<CategoryEntity>> = categoryRepo.categories
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val filteredTransactions: StateFlow<List<TransactionEntity>> = combine(
        transactionRepo.transactions,
        _searchQuery,
        _selectedFilter
    ) { list, query, filter ->
        list.filter { tx ->
            val matchesFilter = when (filter) {
                TransactionFilter.ALL -> true
                TransactionFilter.EXPENSE -> tx.type == TransactionType.EXPENSE
                TransactionFilter.INCOME -> tx.type == TransactionType.INCOME
            }
            val matchesQuery = query.isEmpty() ||
                    tx.title.contains(query, ignoreCase = true) ||
                    tx.categoryName.contains(query, ignoreCase = true) ||
                    tx.note.contains(query, ignoreCase = true)

            matchesFilter && matchesQuery
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setFilter(filter: TransactionFilter) {
        _selectedFilter.value = filter
    }

    fun addTransaction(
        title: String,
        amount: Double,
        type: TransactionType,
        categoryId: String,
        categoryName: String,
        dateMillis: Long,
        note: String
    ) {
        transactionRepo.addTransaction(title, amount, type, categoryId, categoryName, dateMillis, note)
    }

    fun updateTransaction(tx: TransactionEntity) {
        transactionRepo.updateTransaction(tx)
    }

    fun deleteTransaction(id: String) {
        transactionRepo.deleteTransaction(id)
    }
}
