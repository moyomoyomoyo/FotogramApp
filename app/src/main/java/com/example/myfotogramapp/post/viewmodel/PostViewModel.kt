package com.example.myfotogramapp.post.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myfotogramapp.auth.AuthManager
import com.example.myfotogramapp.post.model.NewPost
import com.example.myfotogramapp.post.model.PostEntity
import com.example.myfotogramapp.post.repository.PostRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PostViewModel(private val repository: PostRepository, authManager: AuthManager) : ViewModel() {

    private val currentUserId: StateFlow<Int?> = authManager.userId

    private val _myPosts = MutableStateFlow<List<PostEntity>>(emptyList())
    val myPosts = _myPosts.asStateFlow()

    init {
        viewModelScope.launch {
            currentUserId.collect { userId ->
                if (userId != null) {
                    getMyPosts()
                }
            }
        }
    }

    fun createPost(newPost: NewPost) {
        viewModelScope.launch {
            repository.createPost(newPost)
            getMyPosts()
        }
    }

    private suspend fun getMyPosts() {
        val userId = currentUserId.value
        if (userId != null) {
            val posts = getPostsByAuthor(userId)
            if (posts.isNotEmpty()) {
                _myPosts.value = posts
            }
        }
    }

     suspend fun getPostsByAuthor(userId: Int):List<PostEntity> {
         val posts = repository.getPostsByAuthorId(userId)
         if(posts.isNotEmpty()) {
             return posts
         }
            return emptyList()
    }

     suspend fun getPostById(postId: Int): PostEntity? {
        return try {
            repository.getPostById(postId)
        } catch (e: Exception) {
            Log.e("PostViewModel", "Errore caricamento post: ${e.message}")
            null
        }
    }

}
