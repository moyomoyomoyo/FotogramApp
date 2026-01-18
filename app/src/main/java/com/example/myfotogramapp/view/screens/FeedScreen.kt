package com.example.myfotogramapp.view.screens

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.myfotogramapp.feed.FeedViewModel
import com.example.myfotogramapp.navigation.NavigationViewModel
import com.example.myfotogramapp.post.viewmodel.PostViewModel
import com.example.myfotogramapp.user.viewmodel.UserViewModel
import com.example.myfotogramapp.view.components.LoadingScreen
import com.example.myfotogramapp.view.post.PostListItem

@Composable
fun FeedScreen(
    modifier: Modifier = Modifier,
    postViewModel: PostViewModel,
    navViewModel: NavigationViewModel,
    userViewModel: UserViewModel,
    feedViewModel: FeedViewModel
) {

    val postList = feedViewModel.postsList
    val postWithUsers by userViewModel.postWithUsers.collectAsState()
    val postListRefresh = feedViewModel.newRefresh.collectAsState().value
    val isLoading by feedViewModel.isLoading.collectAsState()
    val hasMore by feedViewModel.hasMore.collectAsState()

    LaunchedEffect(postList.size, postListRefresh) {
        Log.i("FeedScreen", "Post list size changed: ${postList.size}, loading users...")
        Log.i("FeedScreen", "isRefreshing: ${postListRefresh}")
        userViewModel.loadUsersForPosts(postList, postListRefresh)
    }

    for( post in postList) {
        Log.i("FeedScreen", "Post ID: ${post.id}")
    }

    Log.i("FeedScreen", "Post with Users size: ${postWithUsers.size}")


    // Object tracking the State of a LazyRow or LazyColumn
    val lazyColState = rememberLazyListState(
        initialFirstVisibleItemIndex = feedViewModel.firstVisibleItemIndex,
        initialFirstVisibleItemScrollOffset = feedViewModel.firstVisibleItemScrollOffset
    )

    val currentIndex = remember { derivedStateOf { lazyColState.firstVisibleItemIndex } }
    val currentOffset = remember { derivedStateOf { lazyColState.firstVisibleItemScrollOffset } }
    val lastVisibleIndex = remember {
        derivedStateOf { lazyColState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
    }

    // Track scroll position for restoration
    LaunchedEffect(currentIndex.value, currentOffset.value) {
        feedViewModel.saveListScrollState(
            lazyColState.firstVisibleItemIndex,
            lazyColState.firstVisibleItemScrollOffset
        )
    }

    // Trigger fetch when near the bottom
    LaunchedEffect(lastVisibleIndex.value) {
        val lastVisibleIndex = lazyColState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
        val total = lazyColState.layoutInfo.totalItemsCount
        val threshold = total - 3
        if (lastVisibleIndex != null && lastVisibleIndex >= threshold) {
            if (hasMore && !isLoading) {
                val oldestPostId = feedViewModel.postsList.minOfOrNull { it.id }
                Log.i("FeedScreen", "Loading more posts before ID: $oldestPostId")
                feedViewModel.fetchNewPosts(maxPostId = oldestPostId)
            }
        }
    }

    if (isLoading && postWithUsers.isEmpty()) {
        LoadingScreen()
        return
    }

    LazyColumn(
        state = lazyColState,
        modifier = modifier.padding(horizontal = 5.dp)
    ) {

        items(postWithUsers) { item ->
            if (item.isLoadingUser) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(strokeWidth = 2.dp, color = Color(0xFF7D0885))
                }
            } else {
                item.user?.let {
                    PostListItem(
                        post = item.post,
                        user = it,
                        navViewModel = navViewModel,
                        userViewModel = userViewModel,
                        postViewModel = postViewModel,
                        modifier = modifier
                    )
                }
            }
        }

        // fine lista visibile ma ci sono ancora altrio post da caricare
        if (hasMore && isLoading) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) { CircularProgressIndicator(strokeWidth = 2.dp, color = Color(0xFF7D0885)) }
            }
        }

        // fine feed
        if (!hasMore && postWithUsers.isNotEmpty()) {
            item {
                Text(
                    text = "You've reached the end!! You deserved a flower 🌸",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xff7d0885).copy(alpha = 0.5f)
                )
            }
        }
    }
}