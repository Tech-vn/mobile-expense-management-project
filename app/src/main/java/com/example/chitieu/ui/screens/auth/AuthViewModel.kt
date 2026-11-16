package com.example.chitieu.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chitieu.data.local.entity.UserEntity
import com.example.chitieu.data.repository.AuthResult
import com.example.chitieu.di.AppModule
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    private val userRepo = AppModule.userRepository

    private val _currentUser = MutableStateFlow<UserEntity?>(null)
    val currentUser: StateFlow<UserEntity?> = _currentUser

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun login(email: String, password: String) = handle { userRepo.login(email, password) }

    fun register(fullName: String, email: String, password: String) =
        handle { userRepo.register(fullName, email, password) }

    fun logout() {
        _currentUser.value = null
    }

    fun clearError() {
        _errorMessage.value = null
    }

    private fun handle(block: suspend () -> AuthResult) {
        viewModelScope.launch {
            when (val result = block()) {
                is AuthResult.Success -> {
                    _errorMessage.value = null
                    _currentUser.value = result.user
                }
                is AuthResult.Error -> _errorMessage.value = result.message
            }
        }
    }
}
