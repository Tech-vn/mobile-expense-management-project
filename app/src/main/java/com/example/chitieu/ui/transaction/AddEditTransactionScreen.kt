package com.example.chitieu.ui.transaction

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.chitieu.data.local.entity.CategoryEntity
import com.example.chitieu.data.local.entity.TransactionEntity
import com.example.chitieu.data.local.entity.TransactionType
import com.example.chitieu.ui.components.CategoryIcon
import com.example.chitieu.ui.theme.ExpenseColor
import com.example.chitieu.ui.theme.IncomeColor
import com.example.chitieu.ui.theme.PrimaryColor
import com.example.chitieu.util.DateUtils

@Composable
fun AddEditTransactionDialog(
    initialType: TransactionType = TransactionType.EXPENSE,
    editingTransaction: TransactionEntity? = null,
    categories: List<CategoryEntity>,
    onDismiss: () -> Unit,
    onSave: (title: String, amount: Double, type: TransactionType, categoryId: String, categoryName: String, dateMillis: Long, note: String) -> Unit
) {
    var type by remember { mutableStateOf(editingTransaction?.type ?: initialType) }
    var title by remember { mutableStateOf(editingTransaction?.title ?: "") }
    var amountText by remember { mutableStateOf(editingTransaction?.amount?.toLong()?.toString() ?: "") }
    var note by remember { mutableStateOf(editingTransaction?.note ?: "") }
    var dateMillis by remember { mutableStateOf(editingTransaction?.dateMillis ?: System.currentTimeMillis()) }

    val filteredCategories = categories.filter { it.type == type }
    var selectedCategory by remember(type) {
        mutableStateOf(
            filteredCategories.find { it.id == editingTransaction?.categoryId } ?: filteredCategories.firstOrNull()
        )
    }

    var categoryExpanded by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (editingTransaction == null) "Thêm giao dịch mới" else "Chỉnh sửa giao dịch",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Đóng")
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Type TabRow
                TabRow(
                    selectedTabIndex = if (type == TransactionType.EXPENSE) 0 else 1,
                    containerColor = Color(0xFFF0F0F0),
                    modifier = Modifier.height(44.dp)
                ) {
                    Tab(
                        selected = type == TransactionType.EXPENSE,
                        onClick = { type = TransactionType.EXPENSE },
                        text = {
                            Text(
                                "Chi tiêu",
                                fontWeight = FontWeight.Bold,
                                color = if (type == TransactionType.EXPENSE) ExpenseColor else Color.Gray
                            )
                        }
                    )
                    Tab(
                        selected = type == TransactionType.INCOME,
                        onClick = { type = TransactionType.INCOME },
                        text = {
                            Text(
                                "Thu nhập",
                                fontWeight = FontWeight.Bold,
                                color = if (type == TransactionType.INCOME) IncomeColor else Color.Gray
                            )
                        }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Amount Field
                Text("Số tiền (VNĐ)", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = amountText,
                    onValueChange = { amountText = it.filter { char -> char.isDigit() } },
                    placeholder = { Text("0 đ") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PrimaryColor),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Title Field
                Text("Tên giao dịch", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    placeholder = { Text("Ví dụ: Ăn trưa, Nhận lương...") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PrimaryColor),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Category Dropdown
                Text("Danh mục", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                Spacer(modifier = Modifier.height(6.dp))
                Surface(
                    onClick = { categoryExpanded = true },
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFF5F5F5),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .padding(14.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            selectedCategory?.let {
                                CategoryIcon(iconName = it.iconName, colorHex = it.colorHex, size = 28.dp, iconSize = 16.dp)
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(it.name, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                            } ?: Text("Chọn danh mục", color = Color.Gray)
                        }
                        Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null)
                    }

                    DropdownMenu(
                        expanded = categoryExpanded,
                        onDismissRequest = { categoryExpanded = false }
                    ) {
                        filteredCategories.forEach { cat ->
                            DropdownMenuItem(
                                text = {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        CategoryIcon(iconName = cat.iconName, colorHex = cat.colorHex, size = 28.dp, iconSize = 16.dp)
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Text(cat.name)
                                    }
                                },
                                onClick = {
                                    selectedCategory = cat
                                    categoryExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Date Display
                Text("Ngày giao dịch: ${DateUtils.formatDate(dateMillis)}", fontSize = 13.sp, color = Color.Gray)

                Spacer(modifier = Modifier.height(12.dp))

                // Note Field
                Text("Ghi chú (Không bắt buộc)", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = note,
                    onValueChange = { note = it },
                    placeholder = { Text("Nhập ghi chú thêm...") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PrimaryColor),
                    maxLines = 2
                )

                if (errorMessage.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(errorMessage, color = ExpenseColor, fontSize = 12.sp)
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Hủy")
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Button(
                        onClick = {
                            val parsedAmount = amountText.toDoubleOrNull()
                            if (parsedAmount == null || parsedAmount <= 0) {
                                errorMessage = "Vui lòng nhập số tiền hợp lệ (> 0)"
                                return@Button
                            }
                            if (title.isBlank()) {
                                errorMessage = "Vui lòng nhập tên giao dịch"
                                return@Button
                            }
                            val category = selectedCategory
                            if (category == null) {
                                errorMessage = "Vui lòng chọn danh mục"
                                return@Button
                            }

                            onSave(
                                title.trim(),
                                parsedAmount,
                                type,
                                category.id,
                                category.name,
                                dateMillis,
                                note.trim()
                            )
                            onDismiss()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryColor),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Lưu giao dịch", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
