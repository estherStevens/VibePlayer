package com.stevens.software.vibeplayer.player

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stevens.software.vibeplayer.media.MediaRepository
import com.stevens.software.vibeplayer.media.PlaybackManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PlayerViewModel(
    id: String,
    private val mediaRepository: MediaRepository,
    private val playbackManager: PlaybackManager
): ViewModel() {

    private val _isLoading: MutableStateFlow<Boolean> = MutableStateFlow(true)

    val uiState: StateFlow<PlayerUiState> = combine(_isLoading, playbackManager.state)
    { isLoading, playbackState ->
        PlayerUiState(
            isPlaying = playbackState.isPlaying,
            artist = playbackState.artist,
            title = playbackState.title,
            artworkUri = playbackState.artworkUri
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        PlayerUiState(
            isPlaying = false,
            title = "",
            artist = "",
            artworkUri = Uri.EMPTY
        )
    )

    init {
        playById(id)
    }

    fun playById(id: String){
        viewModelScope.launch {
            playbackManager.playById(id)
        }
    }

    fun pause(){
        viewModelScope.launch {
            playbackManager.pause()
        }
    }

    fun resume(){
        viewModelScope.launch {
            playbackManager.resume()
        }
    }

    fun onSkipToNextTrack(){
        viewModelScope.launch {
            playbackManager.skipToNextTrack()
        }
    }

    fun onSkipToPreviousTrack(){
        viewModelScope.launch {
            playbackManager.skipToPreviousTrack()
        }
    }
}

data class PlayerUiState(
    val isPlaying: Boolean,
    val title: String,
    val artist: String,
    val artworkUri: Uri
)
