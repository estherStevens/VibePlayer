package com.stevens.software.vibeplayer

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stevens.software.vibeplayer.media.MediaItem
import com.stevens.software.vibeplayer.media.MediaRepository
import com.stevens.software.vibeplayer.utils.toMinutesSeconds
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class VibePlayerViewModel(private val mediaRepository: MediaRepository): ViewModel() {

    val uiState: StateFlow<VibePlayerState> = mediaRepository.loadMedia().map {
        if(it.isEmpty()) {
            VibePlayerState.Empty
        } else {
            VibePlayerState.Tracks(it.map { track -> track.toUi() })
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        VibePlayerState.Scanning
    )

    fun MediaItem.toUi() = MediaItemUi(
        title = this.title,
        albumArt = this.albumArt,
        artist = this.artist,
        duration = this.duration.toMinutesSeconds()
    ) //todo move to repo

}

sealed interface VibePlayerState{
    data object Scanning: VibePlayerState
    data object Empty: VibePlayerState
    data class Tracks(val tracks: List<MediaItemUi>): VibePlayerState
}

data class MediaItemUi(
    val title: String,
    val albumArt: Uri,
    val duration: String,
    val artist: String
)

