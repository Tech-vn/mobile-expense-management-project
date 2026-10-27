package com.example.chitieu

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.chitieu.data.local.AppDatabase
import com.example.chitieu.data.local.dao.BudgetDao
import com.example.chitieu.data.local.dao.CategoryDao
import com.example.chitieu.data.local.dao.TransactionDao
import com.example.chitieu.data.local.entity.BudgetEntity
import com.example.chitieu.data.local.entity.CategoryEntity
import com.example.chitieu.data.local.entity.TransactionEntity
import com.example.chitieu.data.local.entity.TransactionType
import com.example.chitieu.data.repository.BudgetRepository
import com.example.chitieu.data.repository.CategoryRepository
import com.example.chitieu.data.repository.TransactionRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class RoomDatabaseTest {

    private lateinit var db: AppDatabase
    private lateinit var categoryDao: CategoryDao
    private lateinit var transactionDao: TransactionDao
    private lateinit var budgetDao: BudgetDao

    private lateinit var categoryRepository: CategoryRepository
    private lateinit var transactionRepository: TransactionRepository
    private lateinit var budgetRepository: BudgetRepository

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        categoryDao = db.categoryDao()
        transactionDao = db.transactionDao()
        budgetDao = db.budgetDao()

        categoryRepository = CategoryRepository(categoryDao)
        transactionRepository = TransactionRepository(transactionDao)
        budgetRepository = BudgetRepository(budgetDao)
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun testCategoryInsertAndQueryByType() = runBlocking {
        val cat1 = CategoryEntity(name = "Ăn uống", icon = "restaurant", color = "#FF5722", type = TransactionType.EXPENSE)
        val cat2 = CategoryEntity(name = "Lương", icon = "payments", color = "#4CAF50", type = TransactionType.INCOME)

        val id1 = categoryRepository.insertCategory(cat1)
        val id2 = categoryRepository.insertCategory(cat2)

        assertTrue(id1 > 0)
        assertTrue(id2 > 0)

        val allCategories = categoryRepository.getAllCategories().first()
        assertEquals(2, allCategories.size)

        val expenseCategories = categoryRepository.getCategoriesByType(TransactionType.EXPENSE).first()
        assertEquals(1, expenseCategories.size)
        assertEquals("Ăn uống", expenseCategories[0].name)

        val incomeCategories = categoryRepository.getCategoriesByType(TransactionType.INCOME).first()
        assertEquals(1, incomeCategories.size)
        assertEquals("Lương", incomeCategories[0].name)
    }

    @Test
    fun testTransactionInsertAndCategoryRelation() = runBlocking {
        val catId = categoryRepository.insertCategory(
            CategoryEntity(name = "Ăn uống", icon = "restaurant", color = "#FF5722", type = TransactionType.EXPENSE)
        )

        val transaction = TransactionEntity(
            amount = 50000L,
            note = "Phở bò sáng",
            date = 1710000000000L,
            categoryId = catId,
            type = TransactionType.EXPENSE
        )
        val transId = transactionRepository.insertTransaction(transaction)
        assertTrue(transId > 0)

        val transactions = transactionRepository.getAllTransactionsWithCategory().first()
        assertEquals(1, transactions.size)
        assertEquals("Phở bò sáng", transactions[0].transaction.note)
        assertEquals("Ăn uống", transactions[0].category.name)
    }

    @Test
    fun testForeignKeyCascadeDelete() = runBlocking {
        val catId = categoryRepository.insertCategory(
            CategoryEntity(name = "Mua sắm", icon = "shopping", color = "#E91E63", type = TransactionType.EXPENSE)
        )
        val transId = transactionRepository.insertTransaction(
            TransactionEntity(
                amount = 200000L,
                note = "Quần áo",
                date = 1710000000000L,
                categoryId = catId,
                type = TransactionType.EXPENSE
            )
        )

        val category = categoryRepository.getCategoryById(catId)
        assertNotNull(category)
        categoryRepository.deleteCategory(category!!)

        val transactions = transactionRepository.getAllTransactionsWithCategory().first()
        assertEquals(0, transactions.size)
    }

    @Test
    fun testMonthlySummaryAndExpenseSumByCategory() = runBlocking {
        val catExpense1 = categoryRepository.insertCategory(
            CategoryEntity(name = "Ăn uống", icon = "restaurant", color = "#FF5722", type = TransactionType.EXPENSE)
        )
        val catExpense2 = categoryRepository.insertCategory(
            CategoryEntity(name = "Giải trí", icon = "movie", color = "#9C27B0", type = TransactionType.EXPENSE)
        )
        val catIncome = categoryRepository.insertCategory(
            CategoryEntity(name = "Lương", icon = "payments", color = "#4CAF50", type = TransactionType.INCOME)
        )

        val startDate = 1710000000000L // e.g. Mar 1
        val endDate = 1712000000000L   // e.g. Mar 31
        val midDate = 1711000000000L

        // Transactions inside date range
        transactionRepository.insertTransaction(
            TransactionEntity(amount = 10000000L, note = "Lương tháng 3", date = midDate, categoryId = catIncome, type = TransactionType.INCOME)
        )
        transactionRepository.insertTransaction(
            TransactionEntity(amount = 2000000L, note = "Ăn uống cả tháng", date = midDate, categoryId = catExpense1, type = TransactionType.EXPENSE)
        )
        transactionRepository.insertTransaction(
            TransactionEntity(amount = 1500000L, note = "Xem phim", date = midDate, categoryId = catExpense2, type = TransactionType.EXPENSE)
        )

        // Transaction outside date range
        transactionRepository.insertTransaction(
            TransactionEntity(amount = 500000L, note = "Ăn uống tháng 4", date = endDate + 100000L, categoryId = catExpense1, type = TransactionType.EXPENSE)
        )

        val summary = transactionRepository.getMonthlySummary(startDate, endDate).first()
        assertEquals(10000000L, summary.totalIncome)
        assertEquals(3500000L, summary.totalExpense)

        val categorySums = transactionRepository.getExpenseSumByCategory(startDate, endDate).first()
        assertEquals(2, categorySums.size)
        assertEquals("Ăn uống", categorySums[0].categoryName)
        assertEquals(2000000L, categorySums[0].totalAmount)
        assertEquals("Giải trí", categorySums[1].categoryName)
        assertEquals(1500000L, categorySums[1].totalAmount)
    }

    @Test
    fun testBudgetInsertAndUpdateWithUsage() = runBlocking {
        val catId = categoryRepository.insertCategory(
            CategoryEntity(name = "Ăn uống", icon = "restaurant", color = "#FF5722", type = TransactionType.EXPENSE)
        )

        val month = 3
        val year = 2026
        val startDate = 1710000000000L
        val endDate = 1712000000000L
        val midDate = 1711000000000L

        // Insert Budget
        val budgetId = budgetRepository.insertOrUpdateBudget(
            BudgetEntity(categoryId = catId, amountLimit = 5000000L, month = month, year = year)
        )
        assertTrue(budgetId > 0)

        // Insert Expense Transactions
        transactionRepository.insertTransaction(
            TransactionEntity(amount = 1200000L, note = "Cơm trưa", date = midDate, categoryId = catId, type = TransactionType.EXPENSE)
        )
        transactionRepository.insertTransaction(
            TransactionEntity(amount = 600000L, note = "Tối", date = midDate, categoryId = catId, type = TransactionType.EXPENSE)
        )

        val budgetsWithUsage = budgetRepository.getBudgetsWithUsage(month, year, startDate, endDate).first()
        assertEquals(1, budgetsWithUsage.size)
        val usage = budgetsWithUsage[0]
        assertEquals(catId, usage.categoryId)
        assertEquals(5000000L, usage.amountLimit)
        assertEquals(1800000L, usage.spentAmount)

        // Test budget update using unique constraint conflict resolution
        val updatedBudgetId = budgetRepository.insertOrUpdateBudget(
            BudgetEntity(id = budgetId, categoryId = catId, amountLimit = 6000000L, month = month, year = year)
        )
        assertTrue(updatedBudgetId > 0)
        val updatedBudget = budgetRepository.getBudgetByCategoryAndMonth(catId, month, year)
        assertNotNull(updatedBudget)
        assertEquals(6000000L, updatedBudget!!.amountLimit)
    }
}
