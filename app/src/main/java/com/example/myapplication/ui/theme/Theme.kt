package com.example.myapplication.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val ArchievyDarkColorScheme = darkColorScheme(
    primary          = Color(0xFFE91E63),
    onPrimary        = Color.White,
    secondary        = Color(0xFFFFD54F),
    onSecondary      = Color(0xFF0D0D0D),
    background       = Color(0xFF0D0D0D),
    onBackground     = Color(0xFFF5F5F5),
    surface          = Color(0xFF1A1A1A),
    onSurface        = Color(0xFFF5F5F5),
    surfaceVariant   = Color(0xFF222222),
    onSurfaceVariant = Color(0xFF9E9E9E),
)

@Composable
fun MyApplicationTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = ArchievyDarkColorScheme,
        typography  = Typography,
        content     = content,
    )
}
