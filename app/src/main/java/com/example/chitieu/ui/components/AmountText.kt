package com.example.chitieu.ui.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.example.chitieu.data.local.entity.TransactionType
import com.example.chitieu.ui.theme.ExpenseColor
import com.example.chitieu.ui.theme.IncomeColor
import com.example.chitieu.ui.theme.TextPrimary
import com.example.chitieu.util.CurrencyFormatter

@Composable
fun AmountText(
    amount: Double,
    type: TransactionType?,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 15.sp,
    fontWeight: FontWeight = FontWeight.Bold,
    showPrefix: Boolean = true
) {
    val text = when {
        type == TransactionType.EXPENSE && showPrefix -> CurrencyFormatter.formatSignedVnd(amount, isExpense = true)
        type == TransactionType.INCOME && showPrefix -> CurrencyFormatter.formatSignedVnd(amount, isExpense = false)
        else -> CurrencyFormatter.formatVnd(amount)
    }

    val textColor = when (type) {
        TransactionType.EXPENSE -> ExpenseColor
        TransactionType.INCOME -> IncomeColor
        null -> TextPrimary
    }

    Text(
        text = text,
        color = textColor,
        fontSize = fontSize,
        fontWeight = fontWeight,
        modifier = modifier
    )
}
