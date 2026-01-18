package com.example.myfotogramapp.view.screens

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myfotogramapp.navigation.NavigationViewModel
import com.example.myfotogramapp.post.model.PostEntity
import com.example.myfotogramapp.post.viewmodel.PostViewModel
import com.example.myfotogramapp.user.model.UserEntity
import com.example.myfotogramapp.user.viewmodel.UserViewModel
import com.example.myfotogramapp.view.components.ViewMode
import com.example.myfotogramapp.view.post.PostList
import com.example.myfotogramapp.view.post.PostListItem

@Composable
fun SavedPostsScreen(savedPosts: List<PostEntity>, postViewModel: PostViewModel, userViewModel: UserViewModel, navViewModel: NavigationViewModel, modifier: Modifier){

    val postSavedUsers by userViewModel.postSavedUsers.collectAsState()

    Log.i("SavedPostsScreen", "Saved posts size: ${savedPosts.size}")

    LaunchedEffect(savedPosts.size) {
        userViewModel.loadUsersForSavedPosts(savedPosts)
    }

Log.i("SavedPostsScreen", "Post with Users size: ${postSavedUsers.size}")
    LazyColumn(
        modifier = modifier.padding(horizontal = 5.dp)
    ) {

        items(postSavedUsers) { item ->
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
    }
}