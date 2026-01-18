package com.example.myfotogramapp.application

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myfotogramapp.auth.AuthManager
import com.example.myfotogramapp.settings.SettingsRepository

@Suppress("UNCHECKED_CAST")
class AppViewModelFactory(
    private val settingsRepository: SettingsRepository,
    private val authManager: AuthManager
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AppViewModel::class.java)) {
            return AppViewModel(settingsRepository, authManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
