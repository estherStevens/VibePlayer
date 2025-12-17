package com.stevens.software.vibeplayer.media

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

class MediaProviderImpl(private val context: Context) : MediaProvider{


    override fun getMedia(): Flow<List<MediaItem>> {
        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ALBUM_ID,
            MediaStore.Audio.Media.DURATION
        )

        val cursor = context.contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            null,
        )

        val media: MutableList<MediaItem> = mutableListOf()

        cursor?.use {
            val artist = it.getColumnIndex(MediaStore.Audio.Media.ARTIST)
            val title = it.getColumnIndex(MediaStore.Audio.Media.TITLE)
            val albumIdColumn = it.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)
            val duration = it.getColumnIndex(MediaStore.Audio.Media.DURATION)

            while (it.moveToNext()) {
                val artist = it.getString(artist)
                val title = it.getString(title)
                val albumId = it.getLong(albumIdColumn)
                val albumArtUri = ContentUris.withAppendedId(
                    MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                    albumId
                )
                val duration = it.getLong(duration)
                media.add(
                    MediaItem(
                        artist, title, albumArtUri,  duration.milliseconds
                    )
                )
            }
        }
        return flowOf(media)
    }
}

data class MediaItem(
    val artist: String,
    val title: String,
    val albumArt: Uri,
    val duration: Duration,
)