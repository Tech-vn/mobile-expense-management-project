package com.example.chitieu.data.local

import androidx.room.withTransaction
import com.example.chitieu.data.local.entity.UserEntity
import com.example.chitieu.util.DateUtils
import com.example.chitieu.util.PasswordHasher

object DatabaseSeeder {

    /** Chỉ nạp ở lần chạy đầu tiên (khi bảng users còn trống). */
    suspend fun seedIfFirstRun(db: AppDatabase) {
        if (db.userDao().count() > 0) return
        db.withTransaction {
            val salt = PasswordHasher.newSalt()
            db.userDao().insert(
                UserEntity(
                    id = "user_demo",
                    fullName = SeedData.DEMO_NAME,
                    email = SeedData.DEMO_EMAIL,
                    passwordHash = PasswordHasher.hash(SeedData.DEMO_PASSWORD, salt),
                    salt = salt,
                    createdAtMillis = System.currentTimeMillis()
                )
            )
            insertSampleData(db)
        }
    }

    /** Dùng cho nút "Đặt lại dữ liệu" ở Cài đặt. Không xóa tài khoản đăng nhập. */
    suspend fun resetToDefault(db: AppDatabase) {
        db.withTransaction {
            db.transactionDao().deleteAll()
            db.budgetDao().deleteAll()
            db.categoryDao().deleteAll()
            insertSampleData(db)
        }
    }

    private suspend fun insertSampleData(db: AppDatabase) {
        db.categoryDao().insertAll(SeedData.categories)
        db.transactionDao().insertAll(SeedData.transactions())
        db.budgetDao().insertAll(SeedData.budgets(DateUtils.getCurrentMonthYear()))
    }
}
