package com.vyra.studio

import android.graphics.Color as AndroidColor
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.VideoLibrary
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.vyra.studio.data.AppSettings
import com.vyra.studio.ui.screens.*
import com.vyra.studio.ui.theme.Neon
import com.vyra.studio.ui.theme.ScreenStudioTheme
import com.vyra.studio.ui.theme.appBackground

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(AndroidColor.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(AndroidColor.TRANSPARENT),
        )
        val settings = AppSettings(applicationContext)
        setContent { ScreenStudioTheme { App(settings) } }
    }
}

@Composable
private fun App(settings: AppSettings) {
    var splashDone by rememberSaveable { mutableStateOf(false) }
    val stage = when {
        !splashDone -> 0
        !settings.onboardingDone -> 1
        else -> 2
    }
    Box(Modifier.fillMaxSize().appBackground()) {
        Crossfade(stage, animationSpec = tween(400), label = "stage") { s ->
            when (s) {
                0 -> SplashScreen { splashDone = true }
                1 -> OnboardingScreen { settings.completeOnboarding() }
                else -> MainShell(settings)
            }
        }
    }
}

private enum class Tab(val label: String, val icon: ImageVector) {
    Home("Home", Icons.Rounded.Home),
    Library("Library", Icons.Rounded.VideoLibrary),
    Settings("Settings", Icons.Rounded.Settings),
}

@Composable
private fun MainShell(settings: AppSettings) {
    var tab by rememberSaveable { mutableIntStateOf(0) }
    var feature by rememberSaveable { mutableStateOf<String?>(null) }
    BackHandler(enabled = feature != null) { feature = null }

    Crossfade(feature, animationSpec = tween(250), label = "feature") { f ->
        if (f != null) {
            FeatureScreen(Feature.valueOf(f)) { feature = null }
        } else {
            Column(Modifier.fillMaxSize()) {
                Box(Modifier.weight(1f)) {
                    when (Tab.entries[tab]) {
                        Tab.Home -> HomeScreen(settings) { feature = it.name }
                        Tab.Library -> LibraryScreen { tab = 0 }
                        Tab.Settings -> SettingsScreen(settings)
                    }
                }
                BottomBar(tab) { tab = it }
            }
        }
    }
}

@Composable
private fun BottomBar(selected: Int, onSelect: (Int) -> Unit) {
    Box(Modifier.fillMaxWidth().navigationBarsPadding().padding(horizontal = 16.dp, vertical = 10.dp), contentAlignment = Alignment.Center) {
        Row(
            Modifier
                .widthIn(max = 480.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(28.dp))
                .background(Neon.Surface)
                .border(1.dp, Neon.Border, RoundedCornerShape(28.dp))
                .padding(6.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Tab.entries.forEachIndexed { i, t ->
                val active = i == selected
                val tint by animateColorAsState(if (active) Neon.Text else Neon.TextDim, tween(200), label = "tint")
                val bg by animateColorAsState(if (active) Neon.SurfaceHigh else Neon.Surface, tween(200), label = "bg")
                Column(
                    Modifier
                        .weight(1f)
                        .height(60.dp)
                        .clip(RoundedCornerShape(22.dp))
                        .background(bg)
                        .clickable { onSelect(i) },
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Icon(t.icon, t.label, tint = if (active) Neon.Cyan else tint, modifier = Modifier.size(24.dp))
                    Text(t.label, style = MaterialTheme.typography.labelMedium, color = tint)
                }
            }
        }
    }
}
