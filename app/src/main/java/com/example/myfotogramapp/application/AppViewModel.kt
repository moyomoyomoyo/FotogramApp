package com.example.myfotogramapp.application

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myfotogramapp.auth.AuthManager
import com.example.myfotogramapp.settings.SettingsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AppViewModel(
    private val settingsRepository: SettingsRepository,
    authManager: AuthManager
) : ViewModel() {

    val sessionId: StateFlow<String?> = authManager.sessionId
    val userId: StateFlow<Int?> = authManager.userId

    val isFirstLaunch: StateFlow<Boolean> =
        settingsRepository.isFirstLaunch
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                true
            )

    fun setFirstLaunchDone() {
        viewModelScope.launch {
            settingsRepository.setFirstLaunchDone()
        }
    }
}
