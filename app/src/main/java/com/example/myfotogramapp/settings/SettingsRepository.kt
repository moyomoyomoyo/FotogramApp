package com.example.myfotogramapp.settings

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map

val Context.settingsStore by preferencesDataStore(name = "settings_prefs")

class SettingsRepository(private val context: Context) {

    companion object {
        val FIRST_LAUNCH = booleanPreferencesKey("first_launch")
    }

    val isFirstLaunch = context.settingsStore.data.map { prefs ->
        prefs[FIRST_LAUNCH] ?: true
    }

    suspend fun setFirstLaunchDone() {
        context.settingsStore.edit { prefs ->
            prefs[FIRST_LAUNCH] = false
        }
    }
}
