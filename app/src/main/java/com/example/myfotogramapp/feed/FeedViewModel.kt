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

    private val limit = 20
    private var currentPostId: Int? = null

//    // List state utility
//    var isLoading by mutableStateOf(false)
//        private set

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    var isRefreshing by mutableStateOf(false)
        private set

    private val _hasMore = MutableStateFlow(true)
    val hasMore: StateFlow<Boolean> = _hasMore

    init {
        // ✅ Aspetta che ci sia una sessione valida prima di caricare il feed
        viewModelScope.launch {
            authManager.sessionId
                .filterNotNull() // Aspetta finché sessionId non è null
                .first() // Prendi il primo valore non-null

            Log.i("FeedViewModel", "Sessione valida rilevata, carico feed...")
            fetchNewPosts()
        }
    }


    fun fetchNewPosts(maxPostId: Int? = null) {
        if (_isLoading.value || !_hasMore.value) {
            Log.i("ViewModel", "Fetch aborted: isLoading=${_isLoading.value}")
            return
        }

        viewModelScope.launch {
            _isLoading.value = true

            val fetchFromId = maxPostId ?: currentPostId

            val newPosts = if (fetchFromId != null) {
                Log.i("ViewModel", "Fetching posts before ID: $fetchFromId")
                repository.getFeed(fetchFromId-1, limit = limit)
            } else {
                Log.i("ViewModel", "Fetching initial posts")
                repository.getFeed(limit = limit)
            }

            // ← AGGIUNGI QUESTO
            Log.i("ViewModel", "Received post IDs: ${newPosts.map { it.id }}")
            Log.i("ViewModel", "Received ${newPosts.size} posts (limit=$limit)")

            if (newPosts.isNotEmpty()) {
                postsList = postsList + newPosts

                // aggiorna il cursore con il più vecchio dei nuovi post
                val minId = newPosts.minOf { it.id }
                currentPostId = minId
                Log.i("ViewModel", "Cursor updated to: $currentPostId")

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
            resetFeed()
            //postsRepository.reset()
            fetchNewPosts()
            //delay(2000)
            isRefreshing = false
            Log.i("PostviewModel", "refreshing fatto")
        }
    }

    private fun resetFeed() {
        postsList = emptyList()
        currentPostId = null
        _hasMore.value = true
        fetchNewPosts()
    }

    fun saveListScrollState(index: Int, offset: Int) {
        firstVisibleItemIndex = index
        firstVisibleItemScrollOffset = offset
    }
}
