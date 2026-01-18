package com.example.myfotogramapp.view.profile


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.myfotogramapp.navigation.NavigationViewModel
import com.example.myfotogramapp.post.model.PostEntity
import com.example.myfotogramapp.post.viewmodel.PostViewModel
import com.example.myfotogramapp.user.model.UserEntity
import com.example.myfotogramapp.user.viewmodel.UserViewModel
import com.example.myfotogramapp.view.components.Header
import com.example.myfotogramapp.view.components.ViewMode
import com.example.myfotogramapp.view.post.PostGrid
import com.example.myfotogramapp.view.post.PostList

@Composable
fun ProfileScreen(
    user: UserEntity?,
    posts: List<PostEntity?>,
    navViewModel: NavigationViewModel,
    userViewModel: UserViewModel,
    postViewModel: PostViewModel,
    modifier: Modifier
) {
    var viewMode by remember { mutableStateOf(ViewMode.GRID) }

    Header(navViewModel, user)
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF7d0885).copy(alpha = 0.1f))
    ) {

        // Contenuto scrollabile
        when (viewMode) {
            ViewMode.LIST -> {
                if (user != null) {
                    PostList(
                        user = user,
                        navViewModel = navViewModel,
                        posts = posts,
                        userViewModel = userViewModel,
                        viewMode = viewMode,
                        postViewModel = postViewModel,
                        modifier = modifier,
                        onViewModeChange = { viewMode = it }
                    )
                }
            }
            ViewMode.GRID -> {
                if (user != null) {
                    PostGrid(
                        user = user,
                        navViewModel = navViewModel,
                        posts = posts,
                        userViewModel = userViewModel,
                        viewMode = viewMode,
                        onViewModeChange = { viewMode = it },
                        modifier = modifier
                    )
                }
            }

        }

    }

}

