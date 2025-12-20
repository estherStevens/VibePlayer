package com.stevens.software.vibeplayer.media

import com.stevens.software.vibeplayer.core.AudioFile
import com.stevens.software.vibeplayer.core.AudioFileRepository

class MediaRepository(
    private val mediaProvider: MediaProvider,
    private val playbackManager: PlaybackManager,
    audioFileRepository: AudioFileRepository
) {

    val mediaItems = audioFileRepository.getAllAudioFile()

    suspend fun setMediaPlaylist(audioFiles: List<AudioFile>){
        playbackManager.setPlaylist(audioFiles)
    }

    suspend fun fetchFilteredMedia(
        minFileSizeInMs: Int,
        minFileDurationInMs: Int
    ): Boolean {
       return mediaProvider.fetchMedia(
           minFileSizeInMs = minFileSizeInMs,
           minFileDurationInMs = minFileDurationInMs
       )
    }

    suspend fun fetchAllMedia() {
        mediaProvider.fetchMedia()
    }

}