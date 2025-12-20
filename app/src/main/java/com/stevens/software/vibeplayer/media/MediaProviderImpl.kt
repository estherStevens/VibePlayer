package com.stevens.software.vibeplayer.media

import android.content.ContentUris
import android.content.Context
import android.provider.MediaStore
import com.stevens.software.vibeplayer.core.AudioFile
import com.stevens.software.vibeplayer.core.AudioFileRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.OffsetDateTime

class MediaProviderImpl(
    private val context: Context,
    private val audioFileRepository: AudioFileRepository
    ) : MediaProvider {

    override suspend fun fetchMedia(
        minFileSizeInMs: Int?,
        minFileDurationInMs: Int?
    ): Boolean = withContext(Dispatchers.IO) {
        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ALBUM_ID,
            MediaStore.Audio.Media.DURATION,
        )

        val selection: String?
        val selectionArgs: Array<String>?
        if(minFileSizeInMs == null && minFileDurationInMs == null) {
            selection = null
            selectionArgs = null
        } else {
            selection = "${MediaStore.Audio.Media.DURATION} > ? AND ${MediaStore.Audio.Media.SIZE} > ?"
            selectionArgs = arrayOf(
                (minFileSizeInMs).toString(),
                (minFileDurationInMs?.times(1024)).toString()
            )
        }

        val cursor = context.contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            "${MediaStore.Audio.Media.TITLE} ASC",
        )
        val audioFiles: MutableList<AudioFile> = mutableListOf()

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
                val mediaId = "${id}_${title}_${OffsetDateTime.now()}"
                audioFiles.add(
                    AudioFile(
                        id = mediaId,
                        title = title,
                        artist = artist,
                        duration = duration,
                        artworkUri = albumArtUri.toString(),
                        fileUri = uri.toString()
                    )
                )
            }
        }
        audioFileRepository.deleteAllAudioFiles()
        audioFileRepository.insertAllAudioFiles(audioFiles)
        true
    }
}