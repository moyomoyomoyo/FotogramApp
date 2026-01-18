package com.example.myfotogramapp.view.post

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.myfotogramapp.navigation.NavigationViewModel
import com.example.myfotogramapp.post.model.PostEntity
import com.example.myfotogramapp.post.viewmodel.PostViewModel
import com.example.myfotogramapp.user.model.UserEntity
import com.example.myfotogramapp.user.viewmodel.UserViewModel

@Composable
fun PostDetailContent(navViewModel: NavigationViewModel, post: PostEntity, user: UserEntity, userViewModel: UserViewModel, postViewModel: PostViewModel, modifier: Modifier) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF7d0885).copy(alpha = 0.1f)),
        verticalArrangement = Arrangement.Center
    ) {
        LazyColumn {
            item {
                PostListItem(post, navViewModel, user, userViewModel, postViewModel, modifier )
            }

        }

    }
}