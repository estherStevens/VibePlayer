package com.stevens.software.vibeplayer.media

import kotlinx.coroutines.flow.Flow

interface MediaProvider {
    val mediaItems: Flow<List<AudioItem>>
    suspend fun fetchMedia(minFileSizeInMs: Int? = null,
                           minFileDurationInMs: Int? = null) : Boolean
}