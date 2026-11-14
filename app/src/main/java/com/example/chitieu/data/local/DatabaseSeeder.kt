package com.example.chitieu.data.local

import com.example.chitieu.data.local.entity.BudgetEntity
import com.example.chitieu.data.local.entity.CategoryEntity
import com.example.chitieu.data.local.entity.TransactionEntity
import com.example.chitieu.data.local.entity.TransactionType
import com.example.chitieu.util.DateUtils
import java.util.Calendar

object DatabaseSeeder {

    val defaultCategories = listOf(
        // Expense
        CategoryEntity("cat_1", "Ăn uống", TransactionType.EXPENSE, "Restaurant", "#FF5722", isDefault = true),
        CategoryEntity("cat_2", "Di chuyển", TransactionType.EXPENSE, "DirectionsCar", "#2196F3", isDefault = true),
        CategoryEntity("cat_3", "Mua sắm", TransactionType.EXPENSE, "ShoppingBag", "#E91E63", isDefault = true),
        CategoryEntity("cat_4", "Giải trí", TransactionType.EXPENSE, "Movie", "#9C27B0", isDefault = true),
        CategoryEntity("cat_5", "Hóa đơn & Tiện ích", TransactionType.EXPENSE, "Receipt", "#FF9800", isDefault = true),
        CategoryEntity("cat_6", "Y tế & Sức khỏe", TransactionType.EXPENSE, "LocalHospital", "#F44336", isDefault = true),
        CategoryEntity("cat_7", "Giáo dục", TransactionType.EXPENSE, "School", "#3F51B5", isDefault = true),

        // Income
        CategoryEntity("cat_8", "Tiền lương", TransactionType.INCOME, "Payments", "#4CAF50", isDefault = true),
        CategoryEntity("cat_9", "Tiền thưởng", TransactionType.INCOME, "CardGiftcard", "#009688", isDefault = true),
        CategoryEntity("cat_10", "Đầu tư", TransactionType.INCOME, "TrendingUp", "#00BCD4", isDefault = true),
        CategoryEntity("cat_11", "Thu nhập khác", TransactionType.INCOME, "AttachMoney", "#8BC34A", isDefault = true)
    )

    fun getSampleTransactions(): List<TransactionEntity> {
        val now = System.currentTimeMillis()
        val cal = Calendar.getInstance()

        cal.timeInMillis = now
        cal.add(Calendar.DAY_OF_MONTH, -1)
        val yesterday = cal.timeInMillis

        cal.timeInMillis = now
        cal.add(Calendar.DAY_OF_MONTH, -3)
        val threeDaysAgo = cal.timeInMillis

        cal.timeInMillis = now
        cal.add(Calendar.DAY_OF_MONTH, -5)
        val fiveDaysAgo = cal.timeInMillis

        return listOf(
            TransactionEntity("tx_1", "Nhận lương tháng 3", 25000000.0, TransactionType.INCOME, "cat_8", "Tiền lương", now - 3600000, "Lương công ty"),
            TransactionEntity("tx_2", "Ăn tối cùng bạn bè", 450000.0, TransactionType.EXPENSE, "cat_1", "Ăn uống", now - 7200000, "Nhà hàng BBQ"),
            TransactionEntity("tx_3", "Đổ xăng ô tô", 800000.0, TransactionType.EXPENSE, "cat_2", "Di chuyển", yesterday, "Trạm xăng Petrolimex"),
            TransactionEntity("tx_4", "Mua quần áo mới", 1250000.0, TransactionType.EXPENSE, "cat_3", "Mua sắm", yesterday - 10000000, "Uniqlo Store"),
            TransactionEntity("tx_5", "Thưởng dự án hoàn thành", 5000000.0, TransactionType.INCOME, "cat_9", "Tiền thưởng", threeDaysAgo, "Thưởng Quý 1"),
            TransactionEntity("tx_6", "Tiền điện nước & Internet", 1650000.0, TransactionType.EXPENSE, "cat_5", "Hóa đơn & Tiện ích", threeDaysAgo - 5000000, "Hóa đơn tháng này"),
            TransactionEntity("tx_7", "Xem phim rạp & Bỏng nước", 320000.0, TransactionType.EXPENSE, "cat_4", "Giải trí", fiveDaysAgo, "CGV Cinema")
        )
    }

    fun getSampleBudgets(): List<BudgetEntity> {
        val currentMonthYear = DateUtils.getCurrentMonthYear()
        return listOf(
            BudgetEntity("b_overall", null, 15000000.0, currentMonthYear),
            BudgetEntity("b_1", "cat_1", 4000000.0, currentMonthYear),
            BudgetEntity("b_2", "cat_3", 3000000.0, currentMonthYear),
            BudgetEntity("b_3", "cat_4", 1500000.0, currentMonthYear)
        )
    }
}
