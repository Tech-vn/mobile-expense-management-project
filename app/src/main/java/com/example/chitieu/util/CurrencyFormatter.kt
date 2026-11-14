package com.example.chitieu.util

import java.text.NumberFormat
import java.util.Locale

object CurrencyFormatter {
    var currentCurrencySymbol: String = "VNĐ (₫)"

    fun format(amount: Double): String {
        return try {
            val isUsd = currentCurrencySymbol.contains("USD")
            val isEur = currentCurrencySymbol.contains("EUR")

            val locale = if (isUsd) Locale.US else Locale.Builder().setLanguage("vi").setRegion("VN").build()
            val formatter = NumberFormat.getNumberInstance(locale)

            if (isUsd) {
                "$${formatter.format(amount / 25000.0)}"
            } else if (isEur) {
                "€${formatter.format(amount / 27000.0)}"
            } else {
                "${formatter.format(amount)} đ"
            }
        } catch (_: Exception) {
            "${amount.toLong()} đ"
        }
    }

    fun formatVnd(amount: Double): String = format(amount)

    fun formatSignedVnd(amount: Double, isExpense: Boolean): String {
        val formatted = format(amount)
        return if (isExpense) "- $formatted" else "+ $formatted"
    }
}
