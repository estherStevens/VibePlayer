package com.stevens.software.vibeplayer.media

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class MediaRepository(
    private val mediaProvider: MediaProvider
) {

    fun loadMedia() : Flow<List<MediaItem>>{
        return  mediaProvider.getMedia()
    }
}