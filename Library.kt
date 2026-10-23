package com.vyra.studio.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.VideoLibrary
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vyra.studio.ui.components.EmptyState
import com.vyra.studio.ui.components.GradientButton
import com.vyra.studio.ui.theme.Neon

@Composable
fun LibraryScreen(onGoHome: () -> Unit) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
        Column(
            Modifier.widthIn(max = 640.dp).fillMaxSize().statusBarsPadding().padding(20.dp),
        ) {
            Text("Library", style = MaterialTheme.typography.headlineMedium, color = Neon.Text)
            Spacer(Modifier.height(4.dp))
            Text(
                "Browse, trim, rename and share your recordings. Available from Phase 4.",
                style = MaterialTheme.typography.bodyMedium, color = Neon.TextDim,
            )
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.Center) {
                EmptyState(
                    Icons.Rounded.VideoLibrary,
                    "No recordings yet",
                    "Once recording is available, your videos will be listed here.",
                )
            }
            GradientButton("Go to dashboard", onGoHome)
        }
    }
}
