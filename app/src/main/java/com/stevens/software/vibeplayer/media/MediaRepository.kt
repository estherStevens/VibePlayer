package com.stevens.software.vibeplayer.media

class MediaRepository(
    private val mediaProvider: MediaProvider,
    private val playbackManager: PlaybackManager
) {

    val mediaItems = mediaProvider.mediaItems

    suspend fun setMediaPlaylist(){
        playbackManager.setPlaylist(mediaItems.value)
    }

    suspend fun fetchMedia(){
        mediaProvider.fetchMedia()
    }


}