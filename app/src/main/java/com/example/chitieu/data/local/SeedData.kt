package com.example.chitieu.data.local

class SeedData {
}
package com.example.chitieu.data.local

import com.example.chitieu.data.local.entity.BudgetEntity
import com.example.chitieu.data.local.entity.CategoryEntity
import com.example.chitieu.data.local.entity.TransactionEntity
import com.example.chitieu.data.local.entity.TransactionType

/**
 * Toàn bộ dữ liệu cứng (dữ liệu mẫu) của app. File này chỉ chứa dữ liệu,
 * việc ghi vào database do DatabaseSeeder đảm nhiệm.
 */
object SeedData {

    // Tài khoản demo (khớp với giá trị điền sẵn ở màn Đăng nhập)
    const val DEMO_NAME = "Minh Anh"
    const val DEMO_EMAIL = "demo@chitieu.com"
    const val DEMO_PASSWORD = "123456"

    private const val HOUR = 3_600_000L
    private const val DAY = 24 * HOUR

    val categories: List<CategoryEntity> = listOf(
        // Chi tiêu
        CategoryEntity("cat_1", "Ăn uống", TransactionType.EXPENSE, "Restaurant", "#FF5722", isDefault = true),
        CategoryEntity("cat_2", "Di chuyển", TransactionType.EXPENSE, "DirectionsCar", "#2196F3", isDefault = true),
        CategoryEntity("cat_3", "Mua sắm", TransactionType.EXPENSE, "ShoppingBag", "#E91E63", isDefault = true),
        CategoryEntity("cat_4", "Giải trí", TransactionType.EXPENSE, "Movie", "#9C27B0", isDefault = true),
        CategoryEntity("cat_5", "Hóa đơn & Tiện ích", TransactionType.EXPENSE, "Receipt", "#FF9800", isDefault = true),
        CategoryEntity("cat_6", "Y tế & Sức khỏe", TransactionType.EXPENSE, "LocalHospital", "#F44336", isDefault = true),
        CategoryEntity("cat_7", "Giáo dục", TransactionType.EXPENSE, "School", "#3F51B5", isDefault = true),

        // Thu nhập
        CategoryEntity("cat_8", "Tiền lương", TransactionType.INCOME, "Payments", "#4CAF50", isDefault = true),
        CategoryEntity("cat_9", "Tiền thưởng", TransactionType.INCOME, "CardGiftcard", "#009688", isDefault = true),
        CategoryEntity("cat_10", "Đầu tư", TransactionType.INCOME, "TrendingUp", "#00BCD4", isDefault = true),
        CategoryEntity("cat_11", "Thu nhập khác", TransactionType.INCOME, "AttachMoney", "#8BC34A", isDefault = true)
    )

    // Ngày giao dịch tính lùi từ lúc nạp dữ liệu để số liệu luôn rơi vào tháng hiện tại
    fun transactions(now: Long = System.currentTimeMillis()): List<TransactionEntity> = listOf(
        TransactionEntity("tx_1", "Nhận lương tháng này", 25_000_000.0, TransactionType.INCOME, "cat_8", "Tiền lương", now - 1 * HOUR, "Lương công ty"),
        TransactionEntity("tx_2", "Ăn tối cùng bạn bè", 450_000.0, TransactionType.EXPENSE, "cat_1", "Ăn uống", now - 2 * HOUR, "Nhà hàng BBQ"),
        TransactionEntity("tx_8", "Cà phê sáng", 45_000.0, TransactionType.EXPENSE, "cat_1", "Ăn uống", now - 4 * HOUR, "Highlands Coffee"),
        TransactionEntity("tx_3", "Đổ xăng ô tô", 800_000.0, TransactionType.EXPENSE, "cat_2", "Di chuyển", now - 1 * DAY, "Trạm xăng Petrolimex"),
        TransactionEntity("tx_4", "Mua quần áo mới", 1_250_000.0, TransactionType.EXPENSE, "cat_3", "Mua sắm", now - 1 * DAY - 3 * HOUR, "Uniqlo Store"),
        TransactionEntity("tx_5", "Thưởng dự án hoàn thành", 5_000_000.0, TransactionType.INCOME, "cat_9", "Tiền thưởng", now - 3 * DAY, "Thưởng dự án"),
        TransactionEntity("tx_6", "Tiền điện nước & Internet", 1_650_000.0, TransactionType.EXPENSE, "cat_5", "Hóa đơn & Tiện ích", now - 3 * DAY - 2 * HOUR, "Hóa đơn tháng này"),
        TransactionEntity("tx_7", "Xem phim rạp & Bỏng nước", 320_000.0, TransactionType.EXPENSE, "cat_4", "Giải trí", now - 5 * DAY, "CGV Cinema"),
        TransactionEntity("tx_9", "Học phí khóa tiếng Anh", 1_200_000.0, TransactionType.EXPENSE, "cat_7", "Giáo dục", now - 6 * DAY, "Trung tâm ngoại ngữ"),
        TransactionEntity("tx_10", "Khám sức khỏe định kỳ", 700_000.0, TransactionType.EXPENSE, "cat_6", "Y tế & Sức khỏe", now - 7 * DAY, "Bệnh viện"),
        TransactionEntity("tx_11", "Lãi đầu tư", 800_000.0, TransactionType.INCOME, "cat_10", "Đầu tư", now - 8 * DAY, "Lãi tiết kiệm")
    )

    fun budgets(monthYear: String): List<BudgetEntity> = listOf(
        BudgetEntity("b_overall", null, 15_000_000.0, monthYear),
        BudgetEntity("b_1", "cat_1", 4_000_000.0, monthYear),
        BudgetEntity("b_2", "cat_3", 3_000_000.0, monthYear),
        BudgetEntity("b_3", "cat_4", 1_500_000.0, monthYear)
    )
}
