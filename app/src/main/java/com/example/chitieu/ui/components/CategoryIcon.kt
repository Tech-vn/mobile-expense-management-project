package com.example.chitieu.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt

@Composable
fun CategoryIcon(
    iconName: String,
    colorHex: String,
    modifier: Modifier = Modifier,
    size: Dp = 40.dp,
    iconSize: Dp = 20.dp
) {
    val parsedColor = try {
        Color(colorHex.toColorInt())
    } catch (_: Exception) {
        Color(0xFF107C66)
    }

    val iconVector = when (iconName) {
        "Restaurant" -> Icons.Default.Restaurant
        "DirectionsCar" -> Icons.Default.DirectionsCar
        "ShoppingBag" -> Icons.Default.ShoppingBag
        "Movie" -> Icons.Default.Movie
        "Receipt" -> Icons.Default.Receipt
        "LocalHospital" -> Icons.Default.LocalHospital
        "School" -> Icons.Default.School
        "Payments" -> Icons.Default.Payments
        "CardGiftcard" -> Icons.Default.CardGiftcard
        "TrendingUp" -> Icons.AutoMirrored.Filled.TrendingUp
        "AttachMoney" -> Icons.Default.AttachMoney
        else -> Icons.Default.Category
    }

    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(parsedColor.copy(alpha = 0.15f)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = iconVector,
            contentDescription = null,
            tint = parsedColor,
            modifier = Modifier.size(iconSize)
        )
    }
}
