package com.stevens.software.vibeplayer.core

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface AudioFileDao{

    @Insert
    suspend fun insert(audioFile: AudioFile)

    @Delete
    suspend fun delete(audioFile: AudioFile)

    @Update
    suspend fun update(audioFile: AudioFile)

    @Query("Select * from audiofiles where id = :id")
    fun getAudioFile(id: Int) : Flow<AudioFile>

    @Query("Select * from audiofiles")
    fun getAllAudioFiles() : Flow<List<AudioFile>>
}