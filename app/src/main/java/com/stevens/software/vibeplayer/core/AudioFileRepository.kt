package com.stevens.software.vibeplayer.core

import android.content.Context
import kotlinx.coroutines.flow.Flow

class AudioFileRepository(context: Context) {
    private val audioFileDao = AudioFilesDatabase.getDatabase(context).audioFileDao()

    fun getAllAudioFile(): Flow<List<AudioFile>> = audioFileDao.getAllAudioFiles()

    fun getAudioFile(id: Int): Flow<AudioFile?> = audioFileDao.getAudioFile(id)

    suspend fun insertAllAudioFiles(audioFile: List<AudioFile>) = audioFileDao.insertAll(audioFile)

    suspend fun deleteAudioFile(audioFile: AudioFile) = audioFileDao.delete(audioFile)

    suspend fun updateAudioFile(audioFile: AudioFile) = audioFileDao.update(audioFile)

    suspend fun deleteAllAudioFiles() = audioFileDao.deleteAll()
}