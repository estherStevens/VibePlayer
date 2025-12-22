package com.stevens.software.vibeplayer.media

import android.content.ComponentName
import android.content.Context
import android.net.Uri
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import androidx.media3.session.legacy.PlaybackStateCompat
import com.stevens.software.vibeplayer.core.AudioFile
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class PlaybackManager(
    context: Context
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
            while (isActive) {
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
                            duration = controller.contentDuration,
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
                            artworkUri = media.mediaMetadata.artworkUri ?: Uri.EMPTY,
                            isShuffleModeEnabled = controller.shuffleModeEnabled
                        )
                    }
                }
            }

        })
    }

    suspend fun setPlaylist(items: List<AudioFile>) {
        val mediaItems: MutableList<MediaItem> = mutableListOf()
        items.map { audioItem ->
            mediaItems.add(
                MediaItem.Builder()
                    .setUri(audioItem.fileUri)
                    .setMediaId(audioItem.id)
                    .setMediaMetadata(
                        MediaMetadata.Builder()
                            .setTitle(audioItem.title)
                            .setArtist(audioItem.artist)
                            .setArtworkUri(audioItem.artworkUri.toUri())
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

    private suspend fun playAllFromStart(){
        val controller = awaitController()
        controller.prepare()
        controller.seekTo(0, 0)
        controller.play()
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

    suspend fun enableShuffle(enabled: Boolean){
        val controller = awaitController()
        controller.shuffleModeEnabled = enabled
        _state.update {
            it.copy(isShuffleModeEnabled = enabled)
        }
    }

    suspend fun enableRepeatAll(){
        val controller = awaitController()
        controller.prepare()
        controller.repeatMode = Player.REPEAT_MODE_ALL
        _state.update {
            it.copy(
                repeatMode = RepeatMode.REPEAT_ALL
            )
        }
    }

    suspend fun enableRepeatOne(){
        val controller = awaitController()
        controller.prepare()
        controller.repeatMode = Player.REPEAT_MODE_ONE
        _state.update {
            it.copy(
                repeatMode = RepeatMode.REPEAT_ONE
            )
        }
    }

    suspend fun disableRepeat(){
        val controller = awaitController()
        controller.prepare()
        controller.repeatMode = Player.REPEAT_MODE_OFF
        _state.update {
            it.copy(
                repeatMode = RepeatMode.REPEAT_OFF
            )
        }
    }

    suspend fun seek(position: Long) {
        val controller = awaitController()
        controller.seekTo(position)
    }

    suspend fun play(){
        val controller = awaitController()
        when(controller.shuffleModeEnabled) {
            true -> {
                controller.prepare()
                val firstShuffled = controller.nextMediaItemIndex
                controller.seekTo(firstShuffled, 0)
                controller.play()
            }
            false -> {
                playAllFromStart()
            }
        }
    }
}

data class AudioPlaybackState(
    val currentPosition: Long = 0L,
    val duration: Long = 0L,
    val isPlaying: Boolean = false,
    val title: String = "",
    val artist: String = "",
    val artworkUri: Uri = Uri.EMPTY,
    val isShuffleModeEnabled: Boolean = false,
    val isRepeatAllEnabled: Boolean = false,
    val repeatMode: RepeatMode = RepeatMode.REPEAT_OFF
)

enum class RepeatMode {
    REPEAT_ALL,
    REPEAT_ONE,
    REPEAT_OFF
}