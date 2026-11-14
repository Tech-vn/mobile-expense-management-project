package com.example.chitieu.ui.settings

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.example.chitieu.data.repository.BudgetRepository
import com.example.chitieu.data.repository.CategoryRepository
import com.example.chitieu.data.repository.TransactionRepository
import com.example.chitieu.util.CurrencyFormatter
import com.example.chitieu.util.DateUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SettingsViewModel : ViewModel() {
    private val transactionRepo = TransactionRepository.getInstance()
    private val categoryRepo = CategoryRepository.getInstance()
    private val budgetRepo = BudgetRepository.getInstance()

    private val _darkModeEnabled = MutableStateFlow(false)
    val darkModeEnabled: StateFlow<Boolean> = _darkModeEnabled

    private val _dailyReminderEnabled = MutableStateFlow(true)
    val dailyReminderEnabled: StateFlow<Boolean> = _dailyReminderEnabled

    private val _securityPinEnabled = MutableStateFlow(false)
    val securityPinEnabled: StateFlow<Boolean> = _securityPinEnabled

    private val _currencyUnit = MutableStateFlow(CurrencyFormatter.currentCurrencySymbol)
    val currencyUnit: StateFlow<String> = _currencyUnit

    private val _language = MutableStateFlow("Tiếng Việt")
    val language: StateFlow<String> = _language

    fun toggleDarkMode(enabled: Boolean) {
        _darkModeEnabled.value = enabled
    }

    fun toggleDailyReminder(enabled: Boolean) {
        _dailyReminderEnabled.value = enabled
    }

    fun toggleSecurityPin(enabled: Boolean) {
        _securityPinEnabled.value = enabled
    }

    fun setCurrencyUnit(unit: String) {
        _currencyUnit.value = unit
        CurrencyFormatter.currentCurrencySymbol = unit
    }

    fun setLanguage(lang: String) {
        _language.value = lang
    }

    fun resetData() {
        transactionRepo.resetToDefaultData()
        categoryRepo.resetToDefaultData()
        budgetRepo.resetToDefaultData()
    }

    fun exportDataCsv(context: Context) {
        try {
            val transactions = transactionRepo.getTransactionList()
            val csvBuilder = StringBuilder()
            csvBuilder.append("ID,Tên giao dịch,Số tiền,Loại,Danh mục,Ngày,Ghi chú\n")

            transactions.forEach { tx ->
                val dateStr = DateUtils.formatDate(tx.dateMillis)
                csvBuilder.append("${tx.id},\"${tx.title}\",${tx.amount},${tx.type},\"${tx.categoryName}\",\"$dateStr\",\"${tx.note}\"\n")
            }

            val sendIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, csvBuilder.toString())
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(sendIntent, "Chia sẻ báo cáo giao dịch (CSV)")
            context.startActivity(shareIntent)
        } catch (_: Exception) {
            Toast.makeText(context, "Không thể chia sẻ tệp báo cáo", Toast.LENGTH_SHORT).show()
        }
    }
}
