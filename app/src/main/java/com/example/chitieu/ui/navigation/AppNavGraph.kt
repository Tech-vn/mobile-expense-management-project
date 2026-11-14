package com.example.chitieu.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.chitieu.data.local.entity.TransactionType
import com.example.chitieu.ui.budget.BudgetScreen
import com.example.chitieu.ui.budget.BudgetViewModel
import com.example.chitieu.ui.category.CategoryScreen
import com.example.chitieu.ui.category.CategoryViewModel
import com.example.chitieu.ui.home.HomeScreen
import com.example.chitieu.ui.home.HomeViewModel
import com.example.chitieu.ui.report.ReportScreen
import com.example.chitieu.ui.report.ReportViewModel
import com.example.chitieu.ui.screens.auth.LoginScreen
import com.example.chitieu.ui.settings.SettingsScreen
import com.example.chitieu.ui.settings.SettingsViewModel
import com.example.chitieu.ui.theme.ChitieuTheme
import com.example.chitieu.ui.theme.PrimaryColor
import com.example.chitieu.ui.transaction.AddEditTransactionDialog
import com.example.chitieu.ui.transaction.TransactionListScreen
import com.example.chitieu.ui.transaction.TransactionViewModel

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Home : Screen("home", "Trang chủ", Icons.Default.Home)
    object Transactions : Screen("transactions", "Giao dịch", Icons.AutoMirrored.Filled.ReceiptLong)
    object Budget : Screen("budget", "Ngân sách", Icons.Default.PieChart)
    object Report : Screen("report", "Báo cáo", Icons.Default.BarChart)
    object Category : Screen("category", "Danh mục", Icons.Default.Category)
    object Settings : Screen("settings", "Cài đặt", Icons.Default.Settings)
}

@Composable
fun AppNavGraph() {
    var isLoggedIn by remember { mutableStateOf(false) }
    val settingsViewModel: SettingsViewModel = viewModel()
    val isDarkMode by settingsViewModel.darkModeEnabled.collectAsState()

    ChitieuTheme(darkTheme = isDarkMode) {
        if (!isLoggedIn) {
            LoginScreen(onLoginSuccess = { isLoggedIn = true })
        } else {
            MainAppShell(
                settingsViewModel = settingsViewModel,
                onLogout = { isLoggedIn = false }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppShell(
    settingsViewModel: SettingsViewModel,
    onLogout: () -> Unit
) {
    var currentScreen by remember { mutableStateOf<Screen>(Screen.Home) }

    val homeViewModel: HomeViewModel = viewModel()
    val transactionViewModel: TransactionViewModel = viewModel()
    val categoryViewModel: CategoryViewModel = viewModel()
    val budgetViewModel: BudgetViewModel = viewModel()
    val reportViewModel: ReportViewModel = viewModel()

    val categories by transactionViewModel.categories.collectAsState()

    var showAddDialog by remember { mutableStateOf(false) }
    var addDialogType by remember { mutableStateOf(TransactionType.EXPENSE) }

    if (showAddDialog) {
        AddEditTransactionDialog(
            initialType = addDialogType,
            categories = categories,
            onDismiss = { showAddDialog = false },
            onSave = { title, amount, type, categoryId, categoryName, dateMillis, note ->
                transactionViewModel.addTransaction(
                    title, amount, type, categoryId, categoryName, dateMillis, note
                )
            }
        )
    }

    val screens = listOf(
        Screen.Home,
        Screen.Transactions,
        Screen.Budget,
        Screen.Report,
        Screen.Category,
        Screen.Settings
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = currentScreen.title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                },
                actions = {
                    IconButton(onClick = onLogout) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = "Đăng xuất",
                            tint = PrimaryColor
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp
            ) {
                screens.forEach { screen ->
                    val selected = currentScreen.route == screen.route
                    NavigationBarItem(
                        selected = selected,
                        onClick = { currentScreen = screen },
                        icon = {
                            Icon(
                                imageVector = screen.icon,
                                contentDescription = screen.title,
                                modifier = Modifier.size(20.dp)
                            )
                        },
                        label = {
                            Text(
                                text = screen.title,
                                fontSize = 10.sp,
                                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                                maxLines = 1
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = PrimaryColor,
                            selectedTextColor = PrimaryColor,
                            indicatorColor = PrimaryColor.copy(alpha = 0.12f),
                            unselectedIconColor = Color.Gray,
                            unselectedTextColor = Color.Gray
                        )
                    )
                }
            }
        },
        floatingActionButton = {
            if (currentScreen == Screen.Home || currentScreen == Screen.Transactions) {
                FloatingActionButton(
                    onClick = {
                        addDialogType = TransactionType.EXPENSE
                        showAddDialog = true
                    },
                    containerColor = PrimaryColor,
                    contentColor = Color.White
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Thêm giao dịch")
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (currentScreen) {
                Screen.Home -> HomeScreen(
                    onNavigateToTransactions = { currentScreen = Screen.Transactions },
                    onNavigateToBudget = { currentScreen = Screen.Budget },
                    onNavigateToReport = { currentScreen = Screen.Report },
                    onOpenAddTransaction = { type ->
                        addDialogType = type
                        showAddDialog = true
                    },
                    viewModel = homeViewModel
                )
                Screen.Transactions -> TransactionListScreen(
                    viewModel = transactionViewModel,
                    onOpenAddTransaction = { type ->
                        addDialogType = type
                        showAddDialog = true
                    }
                )
                Screen.Budget -> BudgetScreen(viewModel = budgetViewModel)
                Screen.Report -> ReportScreen(viewModel = reportViewModel)
                Screen.Category -> CategoryScreen(viewModel = categoryViewModel)
                Screen.Settings -> SettingsScreen(
                    viewModel = settingsViewModel,
                    onLogout = onLogout
                )
            }
        }
    }
}
