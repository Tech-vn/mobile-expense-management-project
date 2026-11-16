package com.example.chitieu.ui.screens.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Mail
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chitieu.ui.theme.ExpenseColor
import com.example.chitieu.ui.theme.OutlineColor
import com.example.chitieu.ui.theme.PrimaryColor
import com.example.chitieu.ui.theme.TextSecondary

private fun isStrongPassword(p: String): Boolean =
    p.length >= 8 && p.any { it.isUpperCase() } && p.any { it.isLowerCase() } && p.any { it.isDigit() }

@Composable
fun RegisterScreen(
    errorMessage: String? = null,
    onRegister: (fullName: String, email: String, password: String) -> Unit = { _, _, _ -> },
    onNavigateToLogin: () -> Unit = {}
) {
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmVisible by remember { mutableStateOf(false) }
    var agreed by remember { mutableStateOf(false) }

    val passwordValid = isStrongPassword(password)
    val confirmMismatch = confirmPassword.isNotEmpty() && confirmPassword != password
    val canSubmit = fullName.isNotBlank() && email.isNotBlank() &&
        passwordValid && confirmPassword == password && agreed

    Scaffold(containerColor = Color.White) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.Start
        ) {
            // Logo + tên app
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = PrimaryColor,
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.AccountBalanceWallet,
                        contentDescription = "Logo",
                        tint = Color.White,
                        modifier = Modifier.padding(12.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text("Ví Thông Minh", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    Text("Chi tiêu rõ ràng, sống an tâm", color = TextSecondary, fontSize = 12.sp)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text("Đăng ký tài khoản", fontWeight = FontWeight.Bold, fontSize = 32.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Bắt đầu thói quen chi tiêu chủ động.", color = TextSecondary, fontSize = 14.sp)

            Spacer(modifier = Modifier.height(24.dp))

            RegisterField(
                label = "Họ và tên",
                value = fullName,
                onValueChange = { fullName = it },
                placeholder = "Nhập họ và tên của bạn",
                leadingIcon = Icons.Outlined.Person
            )
            Spacer(modifier = Modifier.height(16.dp))

            RegisterField(
                label = "Email",
                value = email,
                onValueChange = { email = it },
                placeholder = "Nhập email của bạn",
                leadingIcon = Icons.Outlined.Mail,
                keyboardType = KeyboardType.Email
            )
            Spacer(modifier = Modifier.height(16.dp))

            RegisterField(
                label = "Mật khẩu",
                value = password,
                onValueChange = { password = it },
                placeholder = "Tạo mật khẩu",
                leadingIcon = Icons.Outlined.Lock,
                isPassword = true,
                passwordVisible = passwordVisible,
                onTogglePassword = { passwordVisible = !passwordVisible },
                keyboardType = KeyboardType.Password
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                val hintColor =
                    if (password.isNotEmpty() && !passwordValid) ExpenseColor else TextSecondary
                Icon(
                    imageVector = Icons.Outlined.Info,
                    contentDescription = null,
                    tint = hintColor,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    "Ít nhất 8 ký tự, gồm chữ hoa, chữ thường và số.",
                    color = hintColor,
                    fontSize = 12.sp
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            RegisterField(
                label = "Xác nhận mật khẩu",
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                placeholder = "Nhập lại mật khẩu",
                leadingIcon = Icons.Outlined.Lock,
                isPassword = true,
                passwordVisible = confirmVisible,
                onTogglePassword = { confirmVisible = !confirmVisible },
                keyboardType = KeyboardType.Password,
                errorText = if (confirmMismatch) "Mật khẩu xác nhận chưa khớp" else null
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Đồng ý điều khoản
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = agreed,
                    onCheckedChange = { agreed = it },
                    colors = CheckboxDefaults.colors(checkedColor = PrimaryColor)
                )
                val linkStyle = SpanStyle(color = PrimaryColor, fontWeight = FontWeight.Bold)
                Text(
                    text = buildAnnotatedString {
                        append("Tôi đồng ý với ")
                        withStyle(linkStyle) { append("Điều khoản sử dụng") }
                        append(" và ")
                        withStyle(linkStyle) { append("Chính sách bảo mật") }
                        append(" của Ví An.")
                    },
                    color = TextSecondary,
                    fontSize = 14.sp
                )
            }

            // Lỗi từ database (ví dụ email đã tồn tại)
            if (errorMessage != null) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(errorMessage, color = ExpenseColor, fontSize = 13.sp)
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = { onRegister(fullName.trim(), email.trim(), password) },
                enabled = canSubmit,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryColor,
                    disabledContainerColor = PrimaryColor.copy(alpha = 0.4f),
                    disabledContentColor = Color.White
                )
            ) {
                Text("Tạo tài khoản", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Đã có tài khoản? ", color = TextSecondary)
                TextButton(onClick = onNavigateToLogin, contentPadding = PaddingValues(0.dp)) {
                    Text("Đăng nhập", color = PrimaryColor, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun RegisterField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    leadingIcon: ImageVector,
    isPassword: Boolean = false,
    passwordVisible: Boolean = false,
    onTogglePassword: () -> Unit = {},
    keyboardType: KeyboardType = KeyboardType.Text,
    errorText: String? = null
) {
    Text(text = label, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
    Spacer(modifier = Modifier.height(8.dp))
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(placeholder, color = TextSecondary.copy(alpha = 0.7f)) },
        leadingIcon = { Icon(leadingIcon, contentDescription = null, tint = TextSecondary) },
        trailingIcon = if (isPassword) {
            {
                IconButton(onClick = onTogglePassword) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Outlined.Visibility else Icons.Outlined.VisibilityOff,
                        contentDescription = "Hiện/ẩn mật khẩu",
                        tint = TextSecondary
                    )
                }
            }
        } else null,
        visualTransformation = if (isPassword && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        isError = errorText != null,
        supportingText = if (errorText != null) {
            { Text(errorText) }
        } else null,
        singleLine = true,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = PrimaryColor,
            unfocusedBorderColor = OutlineColor,
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White
        )
    )
}
