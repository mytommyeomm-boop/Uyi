package com.vyra.studio.data

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

/** Persistent user preferences (survive app restarts). */
class AppSettings(context: Context) {
    private val sp = context.getSharedPreferences("vyra_settings", Context.MODE_PRIVATE)

    var onboardingDone by mutableStateOf(sp.getBoolean("onboarding_done", false))
        private set
    var resolution by mutableStateOf(sp.getString("resolution", "1080p") ?: "1080p")
        private set
    var fps by mutableStateOf(sp.getInt("fps", 30))
        private set
    var countdown by mutableStateOf(sp.getInt("countdown", 3))
        private set
    var audio by mutableStateOf(sp.getString("audio", "Mic") ?: "Mic")
        private set

    fun completeOnboarding() { onboardingDone = true; sp.edit().putBoolean("onboarding_done", true).apply() }
    fun resetOnboarding() { onboardingDone = false; sp.edit().putBoolean("onboarding_done", false).apply() }
    fun updateResolution(v: String) { resolution = v; sp.edit().putString("resolution", v).apply() }
    fun updateFps(v: Int) { fps = v; sp.edit().putInt("fps", v).apply() }
    fun updateCountdown(v: Int) { countdown = v; sp.edit().putInt("countdown", v).apply() }
    fun updateAudio(v: String) { audio = v; sp.edit().putString("audio", v).apply() }
}
