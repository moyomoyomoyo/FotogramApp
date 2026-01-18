package com.example.myfotogramapp.user.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myfotogramapp.auth.AuthManager
import com.example.myfotogramapp.post.model.PostEntity
import com.example.myfotogramapp.user.model.UserEntity
import com.example.myfotogramapp.user.model.UserInfoUpdateDto
import com.example.myfotogramapp.user.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class PostWithUserState(
    val post: PostEntity,
    val user: UserEntity?,
    val isLoadingUser: Boolean
)

class UserViewModel(private val repository: UserRepository, private val authManager: AuthManager) : ViewModel() {

    val currentUserId: StateFlow<Int?> = authManager.userId

    private val _currentUser = MutableStateFlow<UserEntity?>(null)
    val currentUser: StateFlow<UserEntity?> = _currentUser

    private val _postWithUsers = MutableStateFlow<List<PostWithUserState>>(emptyList())
    val postWithUsers: StateFlow<List<PostWithUserState>> = _postWithUsers

    private val _userFollowState = MutableStateFlow<UserEntity?>(null)
    val userFollowState: StateFlow<UserEntity?> = _userFollowState.asStateFlow()

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
        viewModelScope.launch {
            val create = repository.createUser(authManager)
            if(create) {
                Log.i("UserViewModel", "UTENTE CREATO CON SUCCESSO")
            } else {
                Log.e("UserViewModel", "ERRORE DURANTE LA CREAZIONE DELL'UTENTE")
            }
        }
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

        if(success) {
            refreshCurrentUser()
        }

        return success
    }

    fun refreshCurrentUser() {
        viewModelScope.launch {
            currentUserId.first()?.let { userId ->
                _currentUser.value = repository.getUserById(userId)
            }
        }
    }

    suspend fun getUserById(userId: Int): UserEntity? {
        return try {
            repository.getUserById(userId)
        } catch (e: Exception) {
            Log.e("UserViewModel", "Error fetching user by ID: $userId", e)
            null
        }
    }

    // funzione per caricare lo stato di follow dell'utente che voglio seguire/smettere di seguire
    suspend fun loadUser(userId: Int) {
        val user = repository.getUserById(userId)
        _userFollowState.value = user
    }


    suspend fun follow(userIdToFollow: Int) {
        if (repository.follow(userIdToFollow)) {
            refreshCurrentUser()

            loadUser(userIdToFollow)
            refreshUser(userIdToFollow)
            Log.i("UserViewModel", "Ho refreshato follow: $userIdToFollow")
        }
    }

    suspend fun unfollow(userIdToUnfollow: Int) {
        if (repository.unfollow(userIdToUnfollow)) {
            refreshCurrentUser()
            refreshUser(userIdToUnfollow)
            loadUser(userIdToUnfollow)
            Log.i("UserViewModel", "Ho refreshato follow: $userIdToUnfollow")
        }
    }

    // funzione che data una lista di post carica gli utenti associati
    fun loadUsersForPosts(posts: List<PostEntity>) {
        viewModelScope.launch {
            // 1. Identifica solo i POST NUOVI che non hai già
            val currentPostIds = _postWithUsers.value.map { it.post.id }.toSet()
            val newPosts = posts.filter { it.id !in currentPostIds }

            // 2. Crea gli stati iniziali SOLO per i nuovi
            val newPostStates = newPosts.map { post ->
                PostWithUserState(
                    post = post,
                    user = null,
                    isLoadingUser = true
                )
            }

            // 3. AGGIUNGI invece di sostituire
            _postWithUsers.update { current ->
                current + newPostStates  // Mantieni i vecchi + aggiungi i nuovi
            }

            // 4. Carica gli utenti solo per i nuovi post
            newPosts.forEach { post ->
                launch {
                    val user = repository.getUserById(post.authorId)
                    _postWithUsers.update { list ->
                        list.map {
                            if (it.post.id == post.id) {
                                it.copy(user = user, isLoadingUser = false)
                            } else it
                        }
                    }
                }
            }
        }
    }

    // funzione che refresha un utente specifico di postWithUsers (in seguito a follow/unfollow)
    private fun refreshUser(userId: Int) {
        viewModelScope.launch {
            val updatedUser = repository.getUserById(userId)

            _postWithUsers.update { list ->
                list.map { postWithUser ->
                    if (postWithUser.user?.id == userId) {
                        postWithUser.copy(user = updatedUser)
                    } else {
                        postWithUser
                    }
                }
            }

            Log.i("UserViewModel", "Refreshed user $userId in all posts")
        }
    }

}