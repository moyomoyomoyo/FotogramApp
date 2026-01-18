package com.example.myfotogramapp.feed

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myfotogramapp.auth.AuthManager
import com.example.myfotogramapp.post.model.PostEntity
import com.example.myfotogramapp.post.repository.PostRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class FeedViewModel(
    private val repository: PostRepository,
    private val authManager: AuthManager
) : ViewModel() {

    var postsList by mutableStateOf<List<PostEntity>>(emptyList())
        private set

    // Scroll state
    var firstVisibleItemIndex by mutableIntStateOf(0)
        private set
    var firstVisibleItemScrollOffset by mutableIntStateOf(0)
        private set

    private val limit = 10
//    private var currentPostId: Int? = null

//    // List state utility
//    var isLoading by mutableStateOf(false)
//        private set

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    var isRefreshing by mutableStateOf(false)
        private set

    private val _hasMore = MutableStateFlow(true)
    val hasMore: StateFlow<Boolean> = _hasMore

    private val _newRefresh = MutableStateFlow(false)
    val newRefresh: StateFlow<Boolean> = _newRefresh


    init {
        viewModelScope.launch {
            authManager.sessionId
                .filterNotNull() // Aspetta finché sessionId non è null
                .first() // Prendi il primo valore non-null

            Log.i("FeedViewModel", "Sessione valida rilevata, carico feed...")
            fetchNewPosts()
        }
    }

    fun fetchNewPosts(maxPostId: Int? = 11) {
        if (_isLoading.value || !_hasMore.value) {
            Log.i("ViewModel", "Fetch aborted: isLoading=${_isLoading.value}")
            return
        }

        viewModelScope.launch {
            _isLoading.value = true

            if(maxPostId != null) {

                Log.i("ViewModel", "fetchNewPosts called with maxPostId: $maxPostId")
            } else {
                Log.i("ViewModel", "fetchNewPosts called without maxPostId")
            }

            val newPosts = if (maxPostId != null) {
                Log.i("FeedViewModel", "Fetching posts before ID: $maxPostId")
                repository.getFeed(maxPostId - 1, limit = limit)
            } else {
                Log.i("FeedViewModel", "Fetching latest posts")
                repository.getFeed(limit = limit)
            }

            Log.i("ViewModel", "Received post IDs: ${newPosts.map { it.id }}")
            Log.i("ViewModel", "Received ${newPosts.size} posts (limit=$limit)")

            if (newPosts.isNotEmpty()) {
                postsList = if (maxPostId == null) {
                    Log.i("ViewModel", "Replacing posts list with new posts")
                    newPosts.toList()
                } else {
                    Log.i("viewmodel", "aggiungo")
                    postsList + newPosts

                }

                Log.i("ViewModel", "Posts list updated, total size: ${postsList.size}")

//                // aggiorna il cursore con il più vecchio dei nuovi post
//                val minId = newPosts.minOf { it.id }
//                currentPostId = minId
//                Log.i("ViewModel", "Cursor updated to: $currentPostId")

                if (newPosts.size < limit) {
                    _hasMore.value = false
                    Log.i("ViewModel", "End of feed")
                }
            }

            Log.i("ViewModel", "Total posts in memory: ${postsList.size}")
            _isLoading.value = false
        }
    }

    fun refreshList() {
        if (isRefreshing) return

        viewModelScope.launch {
            isRefreshing = true
            postsList = emptyList()
            _newRefresh.value = true
            //postsRepository.reset()
            fetchNewPosts()
//            currentPostId = null
            //delay(2000)
            isRefreshing = false
            Log.i("PostviewModel", "refreshing fatto")
        }
    }

    fun saveListScrollState(index: Int, offset: Int) {
        firstVisibleItemIndex = index
        firstVisibleItemScrollOffset = offset
    }
}
