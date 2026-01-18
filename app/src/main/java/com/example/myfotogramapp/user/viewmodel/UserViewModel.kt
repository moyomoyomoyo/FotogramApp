package com.example.myfotogramapp.user.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myfotogramapp.auth.AuthManager
import com.example.myfotogramapp.user.model.UserEntity
import com.example.myfotogramapp.user.model.UserInfoUpdateDto
import com.example.myfotogramapp.user.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class UserViewModel(
    private val repository: UserRepository,
    private val authManager: AuthManager
) : ViewModel() {

    val currentUserId: StateFlow<Int?> = authManager.userId

    private val _currentUser = MutableStateFlow<UserEntity?>(null)
    val currentUser: StateFlow<UserEntity?> = _currentUser

    init {
        viewModelScope.launch {
            currentUserId.collect { userId ->
                if (userId != null) {
                    refreshCurrentUser()
                }
            }
        }
    }

    fun createUser() {
        viewModelScope.launch { repository.createUser(authManager) }
    }

    suspend fun updateUserInfo(
        username: String,
        bio: String,
        dateOfBirth: String,
        picture: String = ""
    ): Boolean {
        val userId = authManager.userId.value
        if (userId == null) {
            Log.e("UserViewModel", "USER ID NULL")
            return false
        }
        var success = true

        if (picture.isNotEmpty()) {
            if (repository.updateUserProfilePicture(picture)) {
                Log.i("UserViewModel", "FOTO PROFILO AGGIORNATA CON SUCCESSO")
            } else {
                Log.e("UserViewModel", "ERRORE DURANTE L'AGGIORNAMENTO DELLA FOTO PROFILO")
                success = false
            }
        }

        val newInfo = UserInfoUpdateDto(
            username = username,
            bio = bio,
            dateOfBirth = dateOfBirth,
        )

        if (repository.updateUserInfo(newInfo)) {
            Log.i("UserViewModel", "INFO UTENTE AGGIORNATE CON SUCCESSO")
        } else {
            Log.e("UserViewModel", "ERRORE DURANTE L'AGGIORNAMENTO DELLE INFO UTENTE")
            success = false
        }

        if(success)
        refreshCurrentUser()

        return success
    }

    private fun refreshCurrentUser() {
        viewModelScope.launch {
            currentUserId.first()?.let { userId ->
                _currentUser.value = repository.getUserById(userId)
            }
        }
    }


}