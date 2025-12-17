package com.stevens.software.vibeplayer.media

import kotlinx.coroutines.flow.StateFlow

interface MediaProvider {
    val mediaItems: StateFlow<List<AudioItem>>
    suspend fun fetchMedia()
}