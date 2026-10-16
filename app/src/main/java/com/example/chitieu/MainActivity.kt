package com.example.chitieu

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.chitieu.ui.screens.auth.LoginScreen
import com.example.chitieu.ui.theme.ChitieuTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ChitieuTheme {
                                                LoginScreen()
            }
        }
    }
}