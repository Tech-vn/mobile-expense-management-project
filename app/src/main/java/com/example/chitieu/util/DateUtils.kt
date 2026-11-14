package com.example.chitieu.util

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object DateUtils {
    private val locale = Locale.Builder().setLanguage("vi").setRegion("VN").build()

    fun formatDate(millis: Long, pattern: String = "dd/MM/yyyy"): String {
        val sdf = SimpleDateFormat(pattern, locale)
        return sdf.format(Date(millis))
    }

    fun formatDateTime(millis: Long): String {
        val sdf = SimpleDateFormat("HH:mm - dd/MM/yyyy", locale)
        return sdf.format(Date(millis))
    }

    fun getCurrentMonthYear(): String {
        val sdf = SimpleDateFormat("MM/yyyy", locale)
        return sdf.format(Date())
    }

    fun getMonthYear(millis: Long): String {
        val sdf = SimpleDateFormat("MM/yyyy", locale)
        return sdf.format(Date(millis))
    }

    fun isSameDay(millis1: Long, millis2: Long): Boolean {
        val cal1 = Calendar.getInstance().apply { timeInMillis = millis1 }
        val cal2 = Calendar.getInstance().apply { timeInMillis = millis2 }
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
    }

    fun isThisMonth(millis: Long): Boolean {
        val cal = Calendar.getInstance()
        val currentYear = cal.get(Calendar.YEAR)
        val currentMonth = cal.get(Calendar.MONTH)
        cal.timeInMillis = millis
        return cal.get(Calendar.YEAR) == currentYear && cal.get(Calendar.MONTH) == currentMonth
    }
}
