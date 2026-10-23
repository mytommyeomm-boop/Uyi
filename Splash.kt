package com.vyra.studio.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.vyra.studio.ui.components.LogoMark
import com.vyra.studio.ui.theme.Neon
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onDone: () -> Unit) {
    val progress = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        progress.animateTo(1f, tween(700))
        delay(800)
        onDone()
    }
    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Column(
            Modifier.graphicsLayer {
                alpha = progress.value
                scaleX = 0.8f + 0.2f * progress.value
                scaleY = 0.8f + 0.2f * progress.value
            },
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            LogoMark(120.dp, animated = true)
            Spacer(Modifier.height(24.dp))
            Text("VYRA STUDIO", style = MaterialTheme.typography.headlineMedium, color = Neon.Text)
            Spacer(Modifier.height(6.dp))
            Text("Record · Stream · Create", style = MaterialTheme.typography.bodyMedium, color = Neon.TextDim)
        }
    }
}
