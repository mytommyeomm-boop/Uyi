package com.vyra.studio.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.vyra.studio.ui.components.GradientButton
import com.vyra.studio.ui.theme.Neon
import kotlinx.coroutines.launch

private data class OnboardPage(val icon: ImageVector, val title: String, val body: String)

private val pages = listOf(
    OnboardPage(
        Icons.Rounded.FiberManualRecord, "Record anything",
        "Capture your screen, your camera, or both together, in the quality you choose."
    ),
    OnboardPage(
        Icons.Rounded.Mic, "Clear audio, honest limits",
        "Use your microphone, and internal audio on Android 10 or newer where the app allows it. We always tell you what is and is not available."
    ),
    OnboardPage(
        Icons.Rounded.Lock, "Private by design",
        "Recordings stay on your device unless you choose to share or upload them. Permissions are only asked for when you use a feature."
    ),
)

@Composable
fun OnboardingScreen(onFinish: () -> Unit) {
    val state = rememberPagerState(pageCount = { pages.size })
    val scope = rememberCoroutineScope()
    val last = state.currentPage == pages.lastIndex

    Column(
        Modifier.fillMaxSize().statusBarsPadding().navigationBarsPadding().padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(Modifier.fillMaxWidth().widthIn(max = 640.dp), horizontalArrangement = Arrangement.End) {
            Text(
                "Skip",
                style = MaterialTheme.typography.labelLarge, color = Neon.TextDim,
                modifier = Modifier.clip(CircleShape).clickable(onClick = onFinish).padding(horizontal = 16.dp, vertical = 10.dp),
            )
        }
        HorizontalPager(state, Modifier.weight(1f)) { i ->
            val p = pages[i]
            Column(
                Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Box(
                    Modifier.size(120.dp).clip(CircleShape).background(Neon.Brand),
                    contentAlignment = Alignment.Center,
                ) { Icon(p.icon, null, tint = Color.White, modifier = Modifier.size(56.dp)) }
                Spacer(Modifier.height(32.dp))
                Text(p.title, style = MaterialTheme.typography.headlineLarge, color = Neon.Text, textAlign = TextAlign.Center)
                Spacer(Modifier.height(12.dp))
                Text(
                    p.body, style = MaterialTheme.typography.bodyLarge, color = Neon.TextDim,
                    textAlign = TextAlign.Center, modifier = Modifier.widthIn(max = 420.dp),
                )
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(vertical = 20.dp)) {
            pages.indices.forEach { i ->
                val active = i == state.currentPage
                val w by animateDpAsState(if (active) 28.dp else 8.dp, label = "dotW")
                val c by animateColorAsState(if (active) Neon.Blue else Neon.Border, label = "dotC")
                Box(Modifier.height(8.dp).width(w).clip(CircleShape).background(c))
            }
        }
        Box(Modifier.widthIn(max = 640.dp)) {
            GradientButton(
                text = if (last) "Get started" else "Next",
                onClick = {
                    if (last) onFinish() else scope.launch { state.animateScrollToPage(state.currentPage + 1) }
                },
            )
        }
    }
}
