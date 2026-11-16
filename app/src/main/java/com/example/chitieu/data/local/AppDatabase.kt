package com.example.chitieu.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.chitieu.data.local.dao.BudgetDao
import com.example.chitieu.data.local.dao.CategoryDao
import com.example.chitieu.data.local.dao.TransactionDao
import com.example.chitieu.data.local.dao.UserDao
import com.example.chitieu.data.local.entity.BudgetEntity
import com.example.chitieu.data.local.entity.CategoryEntity
import com.example.chitieu.data.local.entity.TransactionEntity
import com.example.chitieu.data.local.entity.UserEntity

@Database(
    entities = [
        UserEntity::class,
        CategoryEntity::class,
        TransactionEntity::class,
        BudgetEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun categoryDao(): CategoryDao
    abstract fun transactionDao(): TransactionDao
    abstract fun budgetDao(): BudgetDao

    companion object {
        private const val DB_NAME = "chitieu.db"

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DB_NAME
                )
                    // Đang phát triển: đổi schema thì xóa DB cũ tạo lại. Khi nộp bài nên thay bằng Migration.
                    .fallbackToDestructiveMigration(dropAllTables = true)
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}
