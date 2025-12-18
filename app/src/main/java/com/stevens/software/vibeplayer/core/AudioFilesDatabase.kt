package com.stevens.software.vibeplayer.core

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [AudioFile::class], version = 1, exportSchema = false)
abstract class AudioFilesDatabase: RoomDatabase(){

    abstract fun audioFileDao(): AudioFileDao

    companion object {
        @Volatile
        private var Instance: AudioFilesDatabase? = null

        fun getDatabase(context: Context): AudioFilesDatabase {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, AudioFilesDatabase::class.java, "audio_file_database")
                    .build()
                    .also { Instance = it }
            }
        }
    }
}