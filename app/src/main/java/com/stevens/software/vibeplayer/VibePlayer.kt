package com.stevens.software.vibeplayer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.stevens.software.vibeplayer.ui.theme.extendedColours

@Composable
fun VibePlayerScreen() {
    VibePlayerView()
}

@Composable
fun VibePlayerView() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.extendedColours.bg)
    ) {

    }
}