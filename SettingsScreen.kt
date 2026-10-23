package com.vyra.studio.ui.screens

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vyra.studio.data.AppSettings
import com.vyra.studio.ui.components.GlassCard
import com.vyra.studio.ui.components.OptionChip
import com.vyra.studio.ui.components.SecondaryButton
import com.vyra.studio.ui.theme.Neon

@Composable
private fun OptionRow(title: String, labels: List<String>, selectedIndex: Int, onSelect: (Int) -> Unit) {
    Column(Modifier.fillMaxWidth()) {
        Text(title, style = MaterialTheme.typography.labelLarge, color = Neon.TextDim)
        Spacer(Modifier.height(8.dp))
        Row(Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            labels.forEachIndexed { i, l -> OptionChip(l, i == selectedIndex) { onSelect(i) } }
        }
    }
}

@Composable
fun SettingsScreen(settings: AppSettings) {
    val resolutions = listOf("720p", "1080p", "1440p")
    val fpsValues = listOf(30, 60)
    val countdowns = listOf(0, 3, 5, 10)
    val audioKeys = listOf("Mic", "Internal", "Both", "Off")
    val audioLabels = listOf("Microphone", "Internal", "Mic + internal", "Muted")

    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
        Column(
            Modifier.widthIn(max = 640.dp).fillMaxSize().verticalScroll(rememberScrollState())
                .statusBarsPadding().padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            Text("Settings", style = MaterialTheme.typography.headlineMedium, color = Neon.Text)

            GlassCard(Modifier.fillMaxWidth()) {
                Text("Recording defaults", style = MaterialTheme.typography.titleMedium, color = Neon.Text)
                Spacer(Modifier.height(16.dp))
                OptionRow("Resolution", resolutions, resolutions.indexOf(settings.resolution)) {
                    settings.updateResolution(resolutions[it])
                }
                Spacer(Modifier.height(16.dp))
                OptionRow("Frame rate", fpsValues.map { "$it fps" }, fpsValues.indexOf(settings.fps)) {
                    settings.updateFps(fpsValues[it])
                }
                Spacer(Modifier.height(16.dp))
                OptionRow("Countdown", countdowns.map { if (it == 0) "Off" else "$it s" }, countdowns.indexOf(settings.countdown)) {
                    settings.updateCountdown(countdowns[it])
                }
            }

            GlassCard(Modifier.fillMaxWidth()) {
                Text("Audio", style = MaterialTheme.typography.titleMedium, color = Neon.Text)
                Spacer(Modifier.height(16.dp))
                OptionRow("Default source", audioLabels, audioKeys.indexOf(settings.audio)) {
                    settings.updateAudio(audioKeys[it])
                }
                Spacer(Modifier.height(12.dp))
                Text(
                    "Internal audio needs Android 10 or newer and only captures apps that allow it. This choice is saved now and applied when recording arrives in Phase 2.",
                    style = MaterialTheme.typography.bodyMedium, color = Neon.TextDim,
                )
            }

            GlassCard(Modifier.fillMaxWidth()) {
                Text("About", style = MaterialTheme.typography.titleMedium, color = Neon.Text)
                Spacer(Modifier.height(8.dp))
                Text("Vyra Studio 0.1.0 · Phase 1", style = MaterialTheme.typography.bodyMedium, color = Neon.TextDim)
                Spacer(Modifier.height(4.dp))
                Text("Settings are stored on this device only.", style = MaterialTheme.typography.bodyMedium, color = Neon.TextDim)
                Spacer(Modifier.height(16.dp))
                SecondaryButton("Replay intro", onClick = { settings.resetOnboarding() })
            }
            Spacer(Modifier.height(8.dp))
        }
    }
}
