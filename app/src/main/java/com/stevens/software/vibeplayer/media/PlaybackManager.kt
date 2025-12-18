package com.stevens.software.vibeplayer.media

import android.content.ComponentName
import android.content.Context
import android.media.session.PlaybackState
import android.net.Uri
import androidx.core.content.ContextCompat
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CompletableJob
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PlaybackManager(
    private val context: Context
) {
    private val appContext = context.applicationContext
    private val sessionToken = SessionToken(
        appContext,
        ComponentName(appContext, MediaService::class.java)
    )
    private val controllerFuture = MediaController.Builder(appContext, sessionToken).buildAsync()
    private val controllerDeferred = CompletableDeferred<MediaController>()
    private var job: Job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.Main + job)

    private val _position: MutableStateFlow<Long> = MutableStateFlow(0)
    private val _state: MutableStateFlow<AudioPlaybackState> = MutableStateFlow(AudioPlaybackState())
    val state: StateFlow<AudioPlaybackState> = _state.asStateFlow()


    init {
        controllerFuture.addListener(
            {
                val controller = controllerFuture.get()
                controllerDeferred.complete(controller)
                observePlayer(controller)
            },
            ContextCompat.getMainExecutor(appContext)
        )
    }

    private suspend fun awaitController(): MediaController = controllerDeferred.await()

    private fun startUpdatingPosition(controller: MediaController){
        job = scope.launch {
            while (true) {
                if(controller.isPlaying){
                    _state.update {
                        it.copy(
                            currentPosition = controller.currentPosition
                        )
                    }
                    delay(1000)
                }
            }
        }
    }

    fun release() {
        job.cancel()
    }

    private fun observePlayer(controller: MediaController) {
        controller.addListener(object : Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                super.onIsPlayingChanged(isPlaying)
                _state.update {
                    it.copy(isPlaying = isPlaying)
                }
                if(isPlaying) {
                    startUpdatingPosition(controller = controller)
                    _state.update {
                        it.copy(
                            duration = controller.contentDuration
                        )
                    }
                } else {
                    release()
                }
            }

            override fun onPositionDiscontinuity(
                oldPosition: Player.PositionInfo,
                newPosition: Player.PositionInfo,
                reason: Int
            ) {
                super.onPositionDiscontinuity(oldPosition, newPosition, reason)
                if(reason == Player.EVENT_POSITION_DISCONTINUITY) {
                    val i = newPosition.positionMs
                    _position.update { newPosition.positionMs }
                }
            }

            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                super.onMediaItemTransition(mediaItem, reason)
                mediaItem?.let { media ->
                    _state.update {
                        it.copy(
                            title = media.mediaMetadata.title.toString(),
                            artist = media.mediaMetadata.artist.toString(),
                            artworkUri = media.mediaMetadata.artworkUri ?: Uri.EMPTY
                        )
                    }
                }
            }

        })
    }

    suspend fun setPlaylist(items: List<AudioItem>) {
        val mediaItems: MutableList<MediaItem> = mutableListOf()
        items.map { audioItem ->
            mediaItems.add(
                MediaItem.Builder()
                    .setUri(audioItem.uri)
                    .setMediaId(audioItem.id)
                    .setMediaMetadata(
                        MediaMetadata.Builder()
                            .setTitle(audioItem.title)
                            .setArtist(audioItem.artist)
                            .setArtworkUri(audioItem.albumArt)
                            .build()
                    ).build()
            )
        }
        val controller = awaitController()
        controller.setMediaItems(mediaItems)
    }

    suspend fun playById(id: String) {
        val controller = awaitController()
        for (i in 0 until controller.mediaItemCount) {
            val item = controller.getMediaItemAt(i)
            if (item.mediaId == id) {
                controller.seekTo(i, 0)
                controller.play()
                return
            }
        }
    }

    suspend fun pause(){
        val controller = awaitController()
        controller.pause()
    }

    suspend fun resume(){
        val controller = awaitController()
        controller.play()
    }

    suspend fun skipToNextTrack(){
        val controller = awaitController()
        controller.seekToNextMediaItem()
    }

    suspend fun skipToPreviousTrack(){
        val controller = awaitController()
        controller.seekToPrevious()
    }

    suspend fun stop(){
        val controller = awaitController()
        controller.stop()
    }

    suspend fun seek(position: Long) {
        val controller = awaitController()
        controller.seekTo(position)
    }
}

data class AudioPlaybackState(
    val currentPosition: Long = 0L,
    val duration: Long = 0L,
    val isPlaying: Boolean = false,
    val title: String = "",
    val artist: String = "",
    val artworkUri: Uri = Uri.EMPTY,
)