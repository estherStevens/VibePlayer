package com.stevens.software.vibeplayer

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class VibePlayerViewModel: ViewModel() {

    val uiState: MutableStateFlow<VibePlayerState> = MutableStateFlow(
        VibePlayerState.Empty
    )
}

sealed interface VibePlayerState{
    data object Scanning: VibePlayerState
    data object Empty: VibePlayerState
    data object Tracks: VibePlayerState
}