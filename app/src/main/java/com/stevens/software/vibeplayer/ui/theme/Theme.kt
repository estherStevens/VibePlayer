package com.stevens.software.vibeplayer.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf

val localExtendedColors = compositionLocalOf { extendedColors }


@Composable
fun VibePlayerTheme(
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(localExtendedColors provides extendedColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}

val MaterialTheme.extendedColours: ExtendedColours
    @Composable
    @ReadOnlyComposable
    get() = localExtendedColors.current