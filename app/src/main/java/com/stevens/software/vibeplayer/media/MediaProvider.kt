package com.stevens.software.vibeplayer.media

import kotlinx.coroutines.flow.Flow

interface MediaProvider {
    fun getMedia(): Flow<List<MediaItem>>
}