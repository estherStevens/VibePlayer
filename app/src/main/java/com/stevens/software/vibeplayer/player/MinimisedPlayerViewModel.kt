package com.stevens.software.vibeplayer.player

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stevens.software.vibeplayer.media.PlaybackManager
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MinimisedPlayerViewModel(
    private val playbackManager: PlaybackManager
): ViewModel() {

    val uiState: StateFlow<MinimisedPlayerUiState> = playbackManager.state.map {
        MinimisedPlayerUiState(
            albumArt = it.artworkUri,
            trackTitle = it.title,
            artist = it.artist,
            isPlaying = it.isPlaying
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        MinimisedPlayerUiState(
            albumArt = Uri.EMPTY,
            trackTitle = "",
            artist = "",
            isPlaying = false
        )
    )

    fun onPause(){
        viewModelScope.launch {
            playbackManager.pause()
        }
    }

    fun onResume(){
        viewModelScope.launch {
            playbackManager.resume()
        }
    }

    fun onSkipToNextTrack(){
        viewModelScope.launch {
            playbackManager.skipToNextTrack()
        }
    }
}

data class MinimisedPlayerUiState(
    val albumArt: Uri,
    val trackTitle: String,
    val artist: String,
    val isPlaying: Boolean,
)