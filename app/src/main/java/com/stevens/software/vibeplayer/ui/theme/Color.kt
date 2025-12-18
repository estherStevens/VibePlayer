package com.stevens.software.vibeplayer.ui.theme

import androidx.compose.ui.graphics.Color

val Pink = Color(0xFFDE84FF)
val White = Color(0xFFFFFFFF)
val MuteBlue = Color(0xFFA7BBD1)
val Blue = Color(0xFF4C647C)
val DarkBlue = Color(0xFF0A131D)
val Green = Color(0xFFF1FF95)
val Grey = Color(0xFF1A2735)
val LightGrey = Color(0x471A2735)

data class ExtendedColours(
    val buttonPrimary: Color,
    val buttonHover: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val textDisabled: Color,
    val bg: Color,
    val accent: Color,
    val outline: Color,
)


val extendedColors = ExtendedColours(
    buttonPrimary = Pink,
    buttonHover = LightGrey,
    textPrimary = White,
    textSecondary = MuteBlue,
    textDisabled = Blue,
    bg = DarkBlue,
    accent = Green,
    outline = Grey

)