package com.stevens.software.vibeplayer.core

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "audiofiles")
data class AudioFile(
    @PrimaryKey()
    val id: String,
    val title: String,
    val artist: String,
    val duration: Long,
    val artworkUri: String,
    val fileUri: String
)