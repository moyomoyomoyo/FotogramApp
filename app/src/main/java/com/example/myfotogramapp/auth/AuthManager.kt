package com.example.myfotogramapp.auth

import android.content.Context
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class AuthManager(
    private val context: Context
) {

    private val _sessionId = MutableStateFlow<String?>(null)
    val sessionId: StateFlow<String?> = _sessionId

    private val _userId = MutableStateFlow<Int?>(null)
    val userId: StateFlow<Int?> = _userId

    private val _sessionLoaded = MutableStateFlow(false)
    val sessionLoaded: StateFlow<Boolean> = _sessionLoaded


    init {
        CoroutineScope(Dispatchers.IO).launch {
            val data = SessionPreferences.getSession(context).first()
            _sessionId.value = data.sessionId.ifEmpty { null }
            _userId.value = if (data.userId == -1) null else data.userId
            _sessionLoaded.value = true
        }
    }

    suspend fun saveSession(sessionId: String, userId: Int) {
        SessionPreferences.saveSession(context, SessionData(sessionId, userId))
        _sessionId.value = sessionId
        _userId.value = userId

        Log.i("AuthManager", "Session saved: sessionId=$sessionId, userId=$userId")
    }

//    suspend fun clearSession() {
//        SessionPreferences.clearSession(context)
//        _sessionId.value = null
//        _userId.value = null
//    }
}