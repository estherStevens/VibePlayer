package com.stevens.software.vibeplayer.utils

import kotlin.time.Duration

fun Duration.toMinutesSeconds(): String {
    val totalSeconds = this.inWholeSeconds
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return "%d:%02d".format(minutes, seconds)
}