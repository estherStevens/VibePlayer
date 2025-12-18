package com.stevens.software.vibeplayer

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stevens.software.vibeplayer.media.AudioItem
import com.stevens.software.vibeplayer.media.MediaService
import com.stevens.software.vibeplayer.media.MediaRepository
import com.stevens.software.vibeplayer.media.PlaybackManager
import com.stevens.software.vibeplayer.utils.toMinutesSeconds
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class VibePlayerViewModel(
    private val mediaRepository: MediaRepository,
    private val mediaPlayer: MediaService,
    private val playbackManager: PlaybackManager
) : ViewModel() {

    private val _navigationEvents: MutableSharedFlow<VibePlayerNavigationEvents> = MutableSharedFlow()
    val navigationEvents = _navigationEvents.asSharedFlow()

    val uiState: StateFlow<VibePlayerState> = mediaRepository.mediaItems.map {
        if (it.isEmpty()) {
            VibePlayerState.Empty
        } else {
            mediaRepository.setMediaPlaylist(it)
            VibePlayerState.Tracks(it.map { track -> track.toUi() })
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        VibePlayerState.Scanning
    )

    init {
        viewModelScope.launch(Dispatchers.IO) {
            mediaRepository.fetchAllMedia()
        }
    }


    fun AudioItem.toUi() = MediaItemUi(
        id = this.id,
        title = this.title,
        albumArt = this.albumArt,
        artist = this.artist,
        duration = this.duration.toMinutesSeconds()
    ) //todo move to repo

    fun onNavigateToPlayer(id: String){
        viewModelScope.launch {
            _navigationEvents.emit(VibePlayerNavigationEvents.NavigateToPlayer(id))
        }
    }

    fun onNavigateToScanMusic(){
        viewModelScope.launch {
            _navigationEvents.emit(VibePlayerNavigationEvents.NavigateToScanMusic)
        }
    }

}

sealed interface VibePlayerState {
    data object Scanning : VibePlayerState
    data object Empty : VibePlayerState
    data class Tracks(val tracks: List<MediaItemUi>) : VibePlayerState
}

data class MediaItemUi(
    val id: String,
    val title: String,
    val albumArt: Uri,
    val duration: String,
    val artist: String
)

sealed interface VibePlayerNavigationEvents {
    object NavigateToScanMusic : VibePlayerNavigationEvents
    data class NavigateToPlayer(val id: String): VibePlayerNavigationEvents
}
