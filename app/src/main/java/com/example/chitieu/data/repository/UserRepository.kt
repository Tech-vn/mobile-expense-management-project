package com.example.chitieu.data.repository

import android.util.Patterns
import com.example.chitieu.data.local.dao.UserDao
import com.example.chitieu.data.local.entity.UserEntity
import com.example.chitieu.util.PasswordHasher
import java.util.UUID

sealed interface AuthResult {
    data class Success(val user: UserEntity) : AuthResult
    data class Error(val message: String) : AuthResult
}

class UserRepository(private val dao: UserDao) {

    suspend fun register(fullName: String, email: String, password: String): AuthResult {
        val normalizedEmail = email.trim().lowercase()
        if (!Patterns.EMAIL_ADDRESS.matcher(normalizedEmail).matches()) {
            return AuthResult.Error("Email không hợp lệ")
        }
        if (dao.findByEmail(normalizedEmail) != null) {
            return AuthResult.Error("Email này đã được đăng ký")
        }
        val salt = PasswordHasher.newSalt()
        val user = UserEntity(
            id = "user_" + UUID.randomUUID().toString().take(8),
            fullName = fullName.trim(),
            email = normalizedEmail,
            passwordHash = PasswordHasher.hash(password, salt),
            salt = salt,
            createdAtMillis = System.currentTimeMillis()
        )
        dao.insert(user)
        return AuthResult.Success(user)
    }

    suspend fun login(email: String, password: String): AuthResult {
        val user = dao.findByEmail(email.trim().lowercase())
        if (user == null || user.passwordHash != PasswordHasher.hash(password, user.salt)) {
            return AuthResult.Error("Email hoặc mật khẩu không đúng")
        }
        return AuthResult.Success(user)
    }
}
