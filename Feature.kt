package com.vyra.studio.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.vyra.studio.ui.components.GradientButton
import com.vyra.studio.ui.theme.Neon

enum class Feature(
    val title: String,
    val shortTitle: String,
    val icon: ImageVector,
    val phase: Int,
    val blurb: String,
) {
    Screen(
        "Screen recording", "Screen", Icons.Rounded.FiberManualRecord, 2,
        "Record your screen with MediaProjection, pause/resume, countdown, microphone and internal audio, and a floating controller."
    ),
    Camera(
        "Camera recording", "Camera", Icons.Rounded.Videocam, 3,
        "Front and rear camera recording with live preview, zoom, focus and exposure controls."
    ),
    Combo(
        "Screen + Camera", "Screen + Cam", Icons.Rounded.PictureInPicture, 3,
        "Record the screen with a movable, resizable camera overlay in a circle or rounded frame."
    ),
    Live(
        "Go Live", "Go Live", Icons.Rounded.Sensors, 5,
        "Live streaming studio. Custom RTMP comes first; YouTube and Twitch need your own API credentials."
    ),
}

/** Honest placeholder: this module is not built yet, and the screen says so. */
@Composable
fun FeatureScreen(feature: Feature, onBack: () -> Unit) {
    Column(
        Modifier.fillMaxSize().statusBarsPadding().navigationBarsPadding().padding(20.dp),
    ) {
        Box(
            Modifier.size(48.dp).clip(CircleShape).background(Neon.SurfaceHigh).clickable(onClick = onBack),
            contentAlignment = Alignment.Center,
        ) { Icon(Icons.AutoMirrored.Rounded.ArrowBack, "Back", tint = Neon.Text) }

        Column(
            Modifier.weight(1f).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Box(
                Modifier.size(96.dp).clip(CircleShape).background(Neon.Brand),
                contentAlignment = Alignment.Center,
            ) { Icon(feature.icon, null, tint = Color.White, modifier = Modifier.size(44.dp)) }
            Spacer(Modifier.height(24.dp))
            Text(feature.title, style = MaterialTheme.typography.headlineMedium, color = Neon.Text)
            Spacer(Modifier.height(8.dp))
            Text(
                "Planned for Phase ${feature.phase}",
                style = MaterialTheme.typography.labelLarge, color = Neon.Cyan,
            )
            Spacer(Modifier.height(16.dp))
            Text(
                feature.blurb,
                style = MaterialTheme.typography.bodyLarge, color = Neon.TextDim, textAlign = TextAlign.Center,
                modifier = Modifier.widthIn(max = 420.dp),
            )
            Spacer(Modifier.height(12.dp))
            Text(
                "This module is not built yet, so nothing here records or streams.",
                style = MaterialTheme.typography.bodyMedium, color = Neon.TextDim, textAlign = TextAlign.Center,
                modifier = Modifier.widthIn(max = 420.dp),
            )
        }
        Box(Modifier.widthIn(max = 640.dp).align(Alignment.CenterHorizontally)) {
            GradientButton("Back to dashboard", onBack)
        }
    }
}
