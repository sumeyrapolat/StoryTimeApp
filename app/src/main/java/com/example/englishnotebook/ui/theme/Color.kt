package com.example.englishnotebook.ui.theme

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode

//application colors
val Pink =Color(0xFFED7299)
val NewPink = Color(0xFFE95D8a)
val PastelYellow = Color(0xFFF1FDB0)
val PastelPink = Color(0xFFFDB5E5)
val Orange =  Color(0xFFF6CA52)
val LightYellow = Color(0xFFFFFFDF)
val White = Color(0xFFFFFFFF)

val SoftPurple = Color(0xFFB799DA) // Yumuşak Mor
val PastelPurple = Color(0xFFDFB0FF) // Pastel Mor
val CoolBlue = Color(0xFF9EDDEF) // Serin ve sakin bir mavi tonu, genel kullanım için ideal.
val LavenderPurple = Color(0xFFE0B0FF) // Yumuşak ve zarif bir mor tonu, uygulamada sıcaklık ve modernlik katar.
val raspberry = Color(0xFFE30B5C)
val SoftPink = Color(0xFFFFB8E1)

//font colors
val Purple = Color(0xFFB48AE5)
val Blue = Color(0xFF8AA0E5)

val LightPurple = Color(0xFFF6DDFF)
val LightBlue = Color(0xFFB8CEFF)
val cardColor = Color(0xFFFFF8F9)
val SoftBlue = Color(0xFFD1D6FF)
val LightPink = Color(0xFFFFF5FA)
val LightOrange = Color(0xFFFFEEA3)
val SoftGreen = Color(0xFFF1FDB0)
val DarkGreen = Color(0xFFBADD7F)
val FernGreen = Color(0xFF3E8440)
val CuteOrange = Color(0xFFFCDD9D)
val DarkOrange = Color(0xFFF1642E)

val cardGradientColor = Brush.linearGradient(
    colors = listOf(
        Pink,
        PastelPink,
        SoftGreen,
        PastelPink,
        Pink
    ),
    start = Offset(0f, 0f),
    end = Offset(Float.POSITIVE_INFINITY, 0f),
    tileMode = TileMode.Clamp
)


val categoryGradientColor = Brush.linearGradient(
    colors = listOf(
        SoftGreen,
        PastelPink,
        Pink,
        PastelPink, SoftGreen
    ),
    start = Offset(0f, 0f),
    end = Offset( Float.POSITIVE_INFINITY,0f),
    tileMode = TileMode.Clamp
)


val bottomGradient = Brush.linearGradient(
    colors = listOf(
        Pink,
        PastelPink,
        SoftGreen,
        PastelPink,
        Pink
    ),
    start = Offset(0f, 0f),
    end = Offset( Float.POSITIVE_INFINITY,0f),
    tileMode = TileMode.Clamp
)


val drawerGradientColor = Brush.linearGradient(
    colors = listOf(
        Pink,
        PastelPink,
        SoftGreen,
        PastelPink,
        Pink
    ),
    start = Offset(0f, 0f),
    end = Offset( 0f,Float.POSITIVE_INFINITY),
    tileMode = TileMode.Clamp
)
