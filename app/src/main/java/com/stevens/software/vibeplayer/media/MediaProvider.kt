package com.stevens.software.vibeplayer.media

interface MediaProvider {
    suspend fun fetchMedia(minFileSizeInMs: Int? = null,
                           minFileDurationInMs: Int? = null) : Boolean
}