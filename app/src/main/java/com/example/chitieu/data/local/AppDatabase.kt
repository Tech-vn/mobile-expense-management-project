package com.example.chitieu.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.chitieu.data.local.dao.BudgetDao
import com.example.chitieu.data.local.dao.CategoryDao
import com.example.chitieu.data.local.dao.TransactionDao
import com.example.chitieu.data.local.entity.BudgetEntity
import com.example.chitieu.data.local.entity.CategoryEntity
import com.example.chitieu.data.local.entity.TransactionEntity

@Database(
    entities = [
        CategoryEntity::class,
        TransactionEntity::class,
        BudgetEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun categoryDao(): CategoryDao
    abstract fun transactionDao(): TransactionDao
    abstract fun budgetDao(): BudgetDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "chitieu_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
