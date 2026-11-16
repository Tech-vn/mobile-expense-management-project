package com.example.chitieu.di

import android.content.Context
import com.example.chitieu.data.local.AppDatabase
import com.example.chitieu.data.local.DatabaseSeeder
import com.example.chitieu.data.repository.BudgetRepository
import com.example.chitieu.data.repository.CategoryRepository
import com.example.chitieu.data.repository.TransactionRepository
import com.example.chitieu.data.repository.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.launch
import java.util.concurrent.Executors

object AppModule {
    private var appContext: Context? = null

    fun init(context: Context) {
        appContext = context.applicationContext
    }

    // Một luồng duy nhất cho mọi thao tác ghi => các lệnh ghi chạy đúng thứ tự gọi
    private val dbDispatcher = Executors.newSingleThreadExecutor().asCoroutineDispatcher()
    val ioScope = CoroutineScope(SupervisorJob() + dbDispatcher)

    val database: AppDatabase by lazy {
        AppDatabase.getInstance(requireNotNull(appContext) { "Chưa gọi AppModule.init() trong ChiTieuApp" })
    }

    val userRepository: UserRepository by lazy { UserRepository(database.userDao()) }

    val categoryRepository: CategoryRepository by lazy {
        CategoryRepository(database.categoryDao(), database.budgetDao(), ioScope)
    }

    val transactionRepository: TransactionRepository by lazy {
        TransactionRepository(database.transactionDao(), ioScope)
    }

    val budgetRepository: BudgetRepository by lazy {
        BudgetRepository(database.budgetDao(), database.categoryDao(), database.transactionDao(), ioScope)
    }

    fun resetToDefaultData() {
        ioScope.launch { DatabaseSeeder.resetToDefault(database) }
    }
}
