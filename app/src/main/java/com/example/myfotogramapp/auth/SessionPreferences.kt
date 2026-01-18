package com.example.myfotogramapp.auth

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map

val Context.sessionStore by preferencesDataStore(name = "session_prefs")

object SessionPreferences {

    val SESSION_ID = stringPreferencesKey("session_id")
    val USER_ID = intPreferencesKey("user_id")

    fun getSession(context: Context) =
        context.sessionStore.data.map { prefs ->
            SessionData(
                sessionId = prefs[SESSION_ID] ?: "",
                userId = prefs[USER_ID] ?: -1
            )
        }

    suspend fun saveSession(context: Context, session: SessionData) {
        context.sessionStore.edit { prefs ->
            prefs[SESSION_ID] = session.sessionId
            prefs[USER_ID] = session.userId
        }
    }

    suspend fun clearSession(context: Context) {
        context.sessionStore.edit { prefs ->
            prefs[SESSION_ID] = ""
            prefs[USER_ID] = -1
        }
    }
}
