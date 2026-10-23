package com.vyra.studio.ui.screens

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Environment
import android.os.StatFs
import android.text.format.Formatter
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.vyra.studio.data.AppSettings
import com.vyra.studio.ui.components.*
import com.vyra.studio.ui.theme.Neon

private data class StorageInfo(val free: Long, val total: Long)

private fun storageInfo(): StorageInfo {
    val s = StatFs(Environment.getDataDirectory().path)
    return StorageInfo(s.availableBytes, s.totalBytes)
}

private fun networkLabel(ctx: Context): String = try {
    val cm = ctx.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val caps = cm.getNetworkCapabilities(cm.activeNetwork)
    when {
        caps == null -> "Offline"
        caps.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> "Wi-Fi"
        caps.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> "Mobile data"
        caps.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> "Ethernet"
        else -> "Connected"
    }
} catch (e: SecurityException) {
    "Unknown"
}

private fun audioLabel(a: String) = when (a) {
    "Mic" -> "Microphone"
    "Internal" -> "Internal audio"
    "Both" -> "Mic + internal"
    else -> "Muted"
}

@Composable
fun HomeScreen(settings: AppSettings, onOpen: (Feature) -> Unit) {
    val ctx = LocalContext.current
    val storage = remember { storageInfo() }
    val net = remember { networkLabel(ctx) }
    val free = Formatter.formatShortFileSize(ctx, storage.free)
    val usedFraction = (1f - storage.free.toFloat() / storage.total.toFloat()).coerceIn(0f, 1f)

    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
        Column(
            Modifier
                .widthIn(max = 640.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .statusBarsPadding()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            // Header
            Row(verticalAlignment = Alignment.CenterVertically) {
                LogoMark(44.dp)
                Spacer(Modifier.width(14.dp))
                Column {
                    Text("Vyra Studio", style = MaterialTheme.typography.titleLarge, color = Neon.Text)
                    Text("Record · Stream · Create", style = MaterialTheme.typography.bodyMedium, color = Neon.TextDim)
                }
            }

            // Status
            GlassCard(Modifier.fillMaxWidth()) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    PulseDot(Neon.Blue)
                    Spacer(Modifier.width(10.dp))
                    Column {
                        Text("Ready", style = MaterialTheme.typography.titleMedium, color = Neon.Text)
                        Text("No recording or stream active", style = MaterialTheme.typography.bodyMedium, color = Neon.TextDim)
                    }
                }
                Spacer(Modifier.height(16.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    InfoCell(Icons.Rounded.HighQuality, "Quality", "${settings.resolution} · ${settings.fps} fps", Modifier.weight(1f))
                    InfoCell(Icons.Rounded.Mic, "Audio", audioLabel(settings.audio), Modifier.weight(1f))
                }
                Spacer(Modifier.height(10.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    InfoCell(if (net == "Offline") Icons.Rounded.WifiOff else Icons.Rounded.Wifi, "Connection", net, Modifier.weight(1f))
                    InfoCell(Icons.Rounded.Storage, "Free storage", free, Modifier.weight(1f))
                }
            }

            // Primary action
            GradientButton(
                text = "Start screen recording",
                icon = Icons.Rounded.FiberManualRecord,
                height = 72.dp,
                onClick = { onOpen(Feature.Screen) },
            )

            // Secondary actions
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                listOf(Feature.Camera, Feature.Combo, Feature.Live).forEach { f ->
                    GlassCard(
                        Modifier.weight(1f),
                        onClick = { onOpen(f) },
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Box(
                            Modifier.size(48.dp).clip(RoundedCornerShape(16.dp)).background(Neon.SurfaceHigh),
                            contentAlignment = Alignment.Center,
                        ) { Icon(f.icon, null, tint = Neon.Cyan, modifier = Modifier.size(26.dp)) }
                        Spacer(Modifier.height(10.dp))
                        Text(
                            f.shortTitle, style = MaterialTheme.typography.labelLarge,
                            color = Neon.Text, textAlign = TextAlign.Center,
                        )
                    }
                }
            }

            // Recent recordings
            Text("Recent recordings", style = MaterialTheme.typography.titleMedium, color = Neon.Text)
            GlassCard(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                EmptyState(
                    Icons.Rounded.VideoLibrary,
                    "No recordings yet",
                    "Your recordings will show up here after you make one.",
                )
            }

            // Storage
            GlassCard(Modifier.fillMaxWidth()) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Device storage", style = MaterialTheme.typography.titleMedium, color = Neon.Text)
                    Text("$free free", style = MaterialTheme.typography.bodyMedium, color = Neon.TextDim)
                }
                Spacer(Modifier.height(14.dp))
                LinearProgressIndicator(
                    progress = { usedFraction },
                    modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)),
                    color = Neon.Blue,
                    trackColor = Neon.SurfaceHigh,
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    "${(usedFraction * 100).toInt()}% used of ${Formatter.formatShortFileSize(ctx, storage.total)}",
                    style = MaterialTheme.typography.labelMedium, color = Neon.TextDim,
                )
            }
            Spacer(Modifier.height(8.dp))
        }
    }
}
