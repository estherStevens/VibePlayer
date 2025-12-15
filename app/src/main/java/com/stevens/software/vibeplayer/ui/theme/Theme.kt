package com.stevens.software.vibeplayer.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf



@Composable
fun VibePlayerTheme(
    content: @Composable () -> Unit
) {
    val localExtendedColors = compositionLocalOf { extendedColors }

    CompositionLocalProvider(localExtendedColors provides extendedColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}