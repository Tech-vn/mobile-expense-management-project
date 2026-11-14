package com.example.chitieu.ui.settings

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chitieu.ui.theme.ExpenseColor
import com.example.chitieu.ui.theme.PrimaryColor
import com.example.chitieu.ui.theme.TextSecondary

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    onLogout: () -> Unit
) {
    val context = LocalContext.current
    val darkModeEnabled by viewModel.darkModeEnabled.collectAsState()
    val dailyReminderEnabled by viewModel.dailyReminderEnabled.collectAsState()
    val securityPinEnabled by viewModel.securityPinEnabled.collectAsState()
    val currencyUnit by viewModel.currencyUnit.collectAsState()
    val language by viewModel.language.collectAsState()

    var showResetDialog by remember { mutableStateOf(false) }
    var showCurrencyDialog by remember { mutableStateOf(false) }
    var showLanguageDialog by remember { mutableStateOf(false) }

    // Currency Selection Dialog
    if (showCurrencyDialog) {
        val currencies = listOf("VNĐ (₫)", "USD ($)", "EUR (€)")
        AlertDialog(
            onDismissRequest = { showCurrencyDialog = false },
            title = { Text("Chọn đơn vị tiền tệ", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    currencies.forEach { unit ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    viewModel.setCurrencyUnit(unit)
                                    showCurrencyDialog = false
                                    Toast.makeText(context, "Đã chọn đơn vị: $unit", Toast.LENGTH_SHORT).show()
                                }
                                .padding(vertical = 8.dp)
                        ) {
                            RadioButton(
                                selected = currencyUnit == unit,
                                onClick = {
                                    viewModel.setCurrencyUnit(unit)
                                    showCurrencyDialog = false
                                },
                                colors = RadioButtonDefaults.colors(selectedColor = PrimaryColor)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(unit, fontSize = 15.sp)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showCurrencyDialog = false }) {
                    Text("Đóng")
                }
            }
        )
    }

    // Language Selection Dialog
    if (showLanguageDialog) {
        val languages = listOf("Tiếng Việt", "English")
        AlertDialog(
            onDismissRequest = { showLanguageDialog = false },
            title = { Text("Chọn ngôn ngữ ứng dụng", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    languages.forEach { lang ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    viewModel.setLanguage(lang)
                                    showLanguageDialog = false
                                    Toast.makeText(context, "Đã chọn ngôn ngữ: $lang", Toast.LENGTH_SHORT).show()
                                }
                                .padding(vertical = 8.dp)
                        ) {
                            RadioButton(
                                selected = language == lang,
                                onClick = {
                                    viewModel.setLanguage(lang)
                                    showLanguageDialog = false
                                },
                                colors = RadioButtonDefaults.colors(selectedColor = PrimaryColor)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(lang, fontSize = 15.sp)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showLanguageDialog = false }) {
                    Text("Đóng")
                }
            }
        )
    }

    // Reset Data Dialog
    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            title = { Text("Đặt lại dữ liệu", fontWeight = FontWeight.Bold) },
            text = { Text("Bạn có chắc chắn muốn làm mới toàn bộ dữ liệu ứng dụng về trạng thái ban đầu không?") },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.resetData()
                        showResetDialog = false
                        Toast.makeText(context, "Đã làm mới dữ liệu thành công", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ExpenseColor)
                ) {
                    Text("Đồng ý đặt lại")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showResetDialog = false }) {
                    Text("Hủy")
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Spacer(modifier = Modifier.height(4.dp))

        // User Account Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(CircleShape)
                        .background(PrimaryColor),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "NV",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Nguyễn Văn A",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Text(
                        text = "user.chitieu@gmail.com",
                        color = TextSecondary,
                        fontSize = 12.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = PrimaryColor.copy(alpha = 0.12f)
                    ) {
                        Text(
                            text = "Tài khoản Cá nhân Premium",
                            color = PrimaryColor,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }
        }

        // Section 1: Cài đặt chung
        SettingsSection(title = "Cài đặt chung") {
            SettingsItemRow(
                icon = Icons.Default.AttachMoney,
                title = "Đơn vị tiền tệ",
                subtitle = currencyUnit,
                onClick = { showCurrencyDialog = true }
            )
            HorizontalDivider(color = Color(0xFFF0F0F0))
            SettingsItemRow(
                icon = Icons.Default.Language,
                title = "Ngôn ngữ",
                subtitle = language,
                onClick = { showLanguageDialog = true }
            )
        }

        // Section 2: Bảo mật & Thông báo
        SettingsSection(title = "Thông báo & Bảo mật") {
            SettingsSwitchRow(
                icon = Icons.Default.Notifications,
                title = "Nhắc nhở ghi chép hàng ngày",
                checked = dailyReminderEnabled,
                onCheckedChange = {
                    viewModel.toggleDailyReminder(it)
                    Toast.makeText(context, if (it) "Đã bật nhắc nhở hàng ngày" else "Đã tắt nhắc nhở", Toast.LENGTH_SHORT).show()
                }
            )
            HorizontalDivider(color = Color(0xFFF0F0F0))
            SettingsSwitchRow(
                icon = Icons.Default.Lock,
                title = "Khóa ứng dụng (Mã PIN / Vân tay)",
                checked = securityPinEnabled,
                onCheckedChange = {
                    viewModel.toggleSecurityPin(it)
                    Toast.makeText(context, if (it) "Đã kích hoạt bảo mật ứng dụng" else "Đã tắt khóa ứng dụng", Toast.LENGTH_SHORT).show()
                }
            )
            HorizontalDivider(color = Color(0xFFF0F0F0))
            SettingsSwitchRow(
                icon = Icons.Default.DarkMode,
                title = "Chế độ tối (Dark Mode)",
                checked = darkModeEnabled,
                onCheckedChange = { viewModel.toggleDarkMode(it) }
            )
        }

        // Section 3: Quản lý dữ liệu
        SettingsSection(title = "Quản lý dữ liệu") {
            SettingsItemRow(
                icon = Icons.Default.Download,
                title = "Xuất dữ liệu thu chi (CSV / Excel)",
                subtitle = "Tải về báo cáo chi tiết",
                onClick = { viewModel.exportDataCsv(context) }
            )
            HorizontalDivider(color = Color(0xFFF0F0F0))
            SettingsItemRow(
                icon = Icons.Default.Refresh,
                title = "Đặt lại dữ liệu ban đầu",
                subtitle = "Khôi phục trạng thái mặc định",
                titleColor = ExpenseColor,
                onClick = { showResetDialog = true }
            )
        }

        // Section 4: Ứng dụng & Hỗ trợ
        SettingsSection(title = "Thông tin & Hỗ trợ") {
            SettingsItemRow(
                icon = Icons.Default.Info,
                title = "Phiên bản ứng dụng",
                subtitle = "v1.0.0 (Build 2025)",
                onClick = { Toast.makeText(context, "Ví Thông Minh v1.0.0 đã là phiên bản mới nhất", Toast.LENGTH_SHORT).show() }
            )
            HorizontalDivider(color = Color(0xFFF0F0F0))
            SettingsItemRow(
                icon = Icons.Default.Shield,
                title = "Chính sách bảo mật & Điều khoản",
                onClick = {
                    Toast.makeText(context, "Mọi dữ liệu tài chính được lưu an toàn trên thiết bị", Toast.LENGTH_SHORT).show()
                }
            )
        }

        // Logout Button
        Button(
            onClick = onLogout,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = ExpenseColor.copy(alpha = 0.1f)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                contentDescription = null,
                tint = ExpenseColor,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Đăng xuất tài khoản",
                color = ExpenseColor,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun SettingsSection(
    title: String,
    content: @Composable () -> Unit
) {
    Column {
        Text(
            text = title,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = TextSecondary,
            modifier = Modifier.padding(start = 4.dp, bottom = 6.dp)
        )
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Column {
                content()
            }
        }
    }
}

@Composable
fun SettingsItemRow(
    icon: ImageVector,
    title: String,
    subtitle: String? = null,
    titleColor: Color = Color.Black,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(PrimaryColor.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = PrimaryColor,
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = title,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = titleColor
                )
                if (subtitle != null) {
                    Text(
                        text = subtitle,
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                }
            }
        }
        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = Color.Gray,
            modifier = Modifier.size(18.dp)
        )
    }
}

@Composable
fun SettingsSwitchRow(
    icon: ImageVector,
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(PrimaryColor.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = PrimaryColor,
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = title,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp
            )
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = PrimaryColor
            )
        )
    }
}
