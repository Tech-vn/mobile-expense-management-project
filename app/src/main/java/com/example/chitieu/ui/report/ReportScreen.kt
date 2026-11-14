package com.example.chitieu.ui.report

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import com.example.chitieu.data.local.entity.TransactionType
import com.example.chitieu.ui.components.CategoryIcon
import com.example.chitieu.ui.theme.ExpenseColor
import com.example.chitieu.ui.theme.IncomeColor
import com.example.chitieu.ui.theme.PrimaryColor
import com.example.chitieu.ui.theme.TextSecondary
import com.example.chitieu.util.CurrencyFormatter
import com.example.chitieu.util.DateUtils

@Composable
fun ReportScreen(
    viewModel: ReportViewModel
) {
    val summary by viewModel.summary.collectAsState()
    val selectedType by viewModel.selectedType.collectAsState()
    val categoryTotals by viewModel.categoryTotals.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item { Spacer(modifier = Modifier.height(4.dp)) }

        item {
            Text(
                text = "Báo cáo Thống kê (${DateUtils.getCurrentMonthYear()})",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // Overview Balance Summary Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = PrimaryColor),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("Tổng thu nhập", color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
                        Text(
                            CurrencyFormatter.formatVnd(summary.totalIncome),
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text("Tổng chi tiêu", color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
                        Text(
                            CurrencyFormatter.formatVnd(summary.totalExpense),
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Tabs Expense vs Income Breakdown
        item {
            TabRow(
                selectedTabIndex = if (selectedType == TransactionType.EXPENSE) 0 else 1,
                containerColor = Color(0xFFF0F0F0),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp)
                    .clip(RoundedCornerShape(12.dp))
            ) {
                Tab(
                    selected = selectedType == TransactionType.EXPENSE,
                    onClick = { viewModel.setSelectedType(TransactionType.EXPENSE) },
                    text = {
                        Text(
                            "Cơ cấu Chi tiêu",
                            fontWeight = FontWeight.Bold,
                            color = if (selectedType == TransactionType.EXPENSE) ExpenseColor else Color.Gray
                        )
                    }
                )
                Tab(
                    selected = selectedType == TransactionType.INCOME,
                    onClick = { viewModel.setSelectedType(TransactionType.INCOME) },
                    text = {
                        Text(
                            "Cơ cấu Thu nhập",
                            fontWeight = FontWeight.Bold,
                            color = if (selectedType == TransactionType.INCOME) IncomeColor else Color.Gray
                        )
                    }
                )
            }
        }

        // Breakdown List Header
        item {
            Text(
                text = "Phân tích theo danh mục",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }

        if (categoryTotals.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Chưa có dữ liệu phân tích cho mục này", color = TextSecondary)
                }
            }
        } else {
            items(categoryTotals, key = { it.categoryId }) { total ->
                val barColor = try {
                    Color(total.colorHex.toColorInt())
                } catch (_: Exception) {
                    PrimaryColor
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                CategoryIcon(
                                    iconName = total.iconName,
                                    colorHex = total.colorHex,
                                    size = 36.dp,
                                    iconSize = 18.dp
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = total.categoryName,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 14.sp
                                )
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = CurrencyFormatter.formatVnd(total.totalAmount),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = if (selectedType == TransactionType.EXPENSE) ExpenseColor else IncomeColor
                                )
                                Text(
                                    text = "${String.format("%.1f", total.percentage)}%",
                                    fontSize = 12.sp,
                                    color = TextSecondary
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        LinearProgressIndicator(
                            progress = { (total.percentage / 100f).coerceIn(0f, 1f) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp)),
                            color = barColor,
                            trackColor = Color(0xFFE0E0E0)
                        )
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(16.dp)) }
    }
}
