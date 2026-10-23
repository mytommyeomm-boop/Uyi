package com.vyra.studio.ui.theme

import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

object Neon {
    val Bg = Color(0xFF07070D)
    val BgDeep = Color(0xFF0C0C1B)
    val Surface = Color(0xFF12121F)
    val SurfaceHigh = Color(0xFF1B1B30)
    val Border = Color(0xFF2A2A46)
    val Blue = Color(0xFF3D7BFF)
    val Violet = Color(0xFF8B5CF6)
    val Cyan = Color(0xFF22D3EE)
    val Danger = Color(0xFFFF4D6D)
    val Success = Color(0xFF34D399)
    val Text = Color(0xFFF2F3FA)
    val TextDim = Color(0xFF9A9DB5)
    val Brand = Brush.linearGradient(listOf(Blue, Violet))
}

private val AppTypography = Typography(
    headlineLarge = TextStyle(fontSize = 32.sp, lineHeight = 38.sp, fontWeight = FontWeight.Bold, letterSpacing = (-0.5).sp),
    headlineMedium = TextStyle(fontSize = 26.sp, lineHeight = 32.sp, fontWeight = FontWeight.Bold),
    titleLarge = TextStyle(fontSize = 20.sp, lineHeight = 26.sp, fontWeight = FontWeight.SemiBold),
    titleMedium = TextStyle(fontSize = 17.sp, lineHeight = 22.sp, fontWeight = FontWeight.SemiBold),
    bodyLarge = TextStyle(fontSize = 16.sp, lineHeight = 24.sp),
    bodyMedium = TextStyle(fontSize = 14.sp, lineHeight = 20.sp),
    labelLarge = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.SemiBold),
    labelMedium = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Medium, letterSpacing = 0.4.sp),
)

@Composable
fun ScreenStudioTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = Neon.Blue,
            secondary = Neon.Violet,
            background = Neon.Bg,
            surface = Neon.Surface,
            onPrimary = Color.White,
            onBackground = Neon.Text,
            onSurface = Neon.Text,
            error = Neon.Danger,
        ),
        typography = AppTypography,
        content = content,
    )
}

/** App-wide dark gradient with a soft violet glow in the top-right corner. */
fun Modifier.appBackground(): Modifier = this
    .background(Brush.verticalGradient(listOf(Neon.BgDeep, Neon.Bg)))
    .drawBehind {
        drawRect(
            Brush.radialGradient(
                colors = listOf(Neon.Violet.copy(alpha = 0.20f), Color.Transparent),
                center = Offset(size.width * 0.85f, 0f),
                radius = size.width * 0.9f,
            )
        )
    }
