package com.example.myfotogramapp.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myfotogramapp.auth.AuthManager
import com.example.myfotogramapp.post.repository.PostRepository

@Suppress("UNCHECKED_CAST")
class FeedViewModelFactory(private val repository: PostRepository, private val authManager: AuthManager) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FeedViewModel::class.java)) {
            return FeedViewModel(repository, authManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
