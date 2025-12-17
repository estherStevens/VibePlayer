package com.stevens.software.vibeplayer.media

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

class MediaProviderImpl(private val context: Context) : MediaProvider{

    private val _mediaItems: MutableStateFlow<List<AudioItem>> = MutableStateFlow(emptyList())
    override val mediaItems = _mediaItems.asStateFlow()

    override suspend fun fetchMedia() {
        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ALBUM_ID,
            MediaStore.Audio.Media.DURATION,
        )

        val cursor = context.contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            "${MediaStore.Audio.Media.TITLE} ASC",
        )

        val media: MutableList<AudioItem> = mutableListOf()

        cursor?.use {
            val id = it.getColumnIndex(MediaStore.Audio.Media._ID)
            val artist = it.getColumnIndex(MediaStore.Audio.Media.ARTIST)
            val title = it.getColumnIndex(MediaStore.Audio.Media.TITLE)
            val albumIdColumn = it.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)
            val duration = it.getColumnIndex(MediaStore.Audio.Media.DURATION)

            while (it.moveToNext()) {
                val id = it.getLong(id)
                val artist = it.getString(artist)
                val title = it.getString(title)
                val albumId = it.getLong(albumIdColumn)
                val albumArtUri = ContentUris.withAppendedId(
                    MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                    albumId
                )
                val duration = it.getLong(duration)
                val uri =  ContentUris.withAppendedId(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id
                )
                val mediaId = "${id}_${title}"

                media.add(
                    AudioItem(
                        mediaId, uri, artist, title, albumArtUri,  duration.milliseconds
                    )
                )
            }
        }
        _mediaItems.value = media
    }
}

data class AudioItem(
    val id: String,
    val uri: Uri,
    val artist: String,
    val title: String,
    val albumArt: Uri,
    val duration: Duration,
)