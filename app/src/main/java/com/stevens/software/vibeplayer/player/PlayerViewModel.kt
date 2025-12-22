package com.stevens.software.vibeplayer.player

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stevens.software.vibeplayer.media.MediaRepository
import com.stevens.software.vibeplayer.media.PlaybackManager
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PlayerViewModel(
    id: String,
    private val mediaRepository: MediaRepository,
    private val playbackManager: PlaybackManager
): ViewModel() {

    private val _isLoading: MutableStateFlow<Boolean> = MutableStateFlow(true)
    private val _navigationEvents: MutableSharedFlow<PlayerNavigationEvents> = MutableSharedFlow()
    val navigationEvents = _navigationEvents.asSharedFlow()

    val uiState: StateFlow<PlayerUiState> = combine(_isLoading, playbackManager.state)
    { isLoading, playbackState ->
        PlayerUiState(
            currentPosition = playbackState.currentPosition,
            duration = playbackState.duration,
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
            currentPosition = 0,
            duration = 0L,
            title = "",
            artist = "",
            artworkUri = Uri.EMPTY
        )
    )

    init {
        when {
            id.isEmpty() -> playFromBeginning()
            else -> playById(id)
        }
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

    fun onBack(){
        viewModelScope.launch {
            _navigationEvents.emit(PlayerNavigationEvents.NavigateBack)
            playbackManager.pause()
        }
    }

    fun onSeek(position: Long){
        viewModelScope.launch {
            playbackManager.seek(position)
        }
    }

    private fun playFromBeginning(){
        viewModelScope.launch {
            playbackManager.playAllFromStart()
        }
    }

    override fun onCleared() {
        super.onCleared()
        playbackManager.release()
    }
}

data class PlayerUiState(
    val currentPosition: Long,
    val duration: Long,
    val isPlaying: Boolean,
    val title: String,
    val artist: String,
    val artworkUri: Uri
)

sealed interface PlayerNavigationEvents {
    object NavigateBack: PlayerNavigationEvents
}