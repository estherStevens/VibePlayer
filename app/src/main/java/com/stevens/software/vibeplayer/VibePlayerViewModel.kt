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
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class VibePlayerViewModel(
    private val mediaRepository: MediaRepository,
    private val mediaPlayer: MediaService,
    private val playbackManager: PlaybackManager
    ): ViewModel() {

    val uiState: StateFlow<VibePlayerState> = mediaRepository.mediaItems.map {
        if(it.isEmpty()) {
            VibePlayerState.Empty
        } else {
            mediaRepository.setMediaPlaylist()
            VibePlayerState.Tracks(it.map { track -> track.toUi() })
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        VibePlayerState.Scanning
    )

    init {
        viewModelScope.launch(Dispatchers.IO) {
            mediaRepository.fetchMedia()
        }
    }


    fun AudioItem.toUi() = MediaItemUi(
        title = this.title,
        albumArt = this.albumArt,
        artist = this.artist,
        duration = this.duration.toMinutesSeconds()
    ) //todo move to repo

     fun play(){
         viewModelScope.launch {
             playbackManager.play()
         }

    }



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

