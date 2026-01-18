package com.example.myfotogramapp.view.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.myfotogramapp.navigation.NavigationViewModel
import com.example.myfotogramapp.navigation.Screen
import com.example.myfotogramapp.post.viewmodel.PostViewModel
import com.example.myfotogramapp.user.viewmodel.UserViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import com.example.myfotogramapp.feed.FeedViewModel
import com.example.myfotogramapp.view.components.Header
import com.example.myfotogramapp.view.components.LoadingScreen
import com.example.myfotogramapp.view.post.PostDetail
import com.example.myfotogramapp.view.profile.ProfileScreen
import com.example.myfotogramapp.view.profile.ProfileSettings
import com.example.myfotogramapp.view.profile.ProfileUser

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navViewModel: NavigationViewModel, userViewModel: UserViewModel, postViewModel: PostViewModel, feedViewModel: FeedViewModel, modifier: Modifier) {

    val currentUser by userViewModel.currentUser.collectAsState()
    val myPosts = postViewModel.myPosts.collectAsState(initial = emptyList()).value

    currentUser?.let{
        when(navViewModel.currentScreen) {
            Screen.FEED -> {
                Header(navViewModel)
                PullToRefreshBox(
                    modifier = modifier.fillMaxSize().background(Color(0xff7d0885).copy(alpha=0.1f)),
                    isRefreshing = feedViewModel.isRefreshing,
                    onRefresh = { feedViewModel.refreshList() },
                ) {
                    FeedScreen (modifier = modifier, postViewModel = postViewModel, navViewModel = navViewModel, feedViewModel = feedViewModel,userViewModel = userViewModel)
                }
            }
            Screen.PROFILE -> {
                ProfileScreen(it, myPosts, navViewModel, userViewModel, postViewModel, modifier)
            }
            Screen.CREATE_POST -> {
                CreatePostScreen(navViewModel, postViewModel, userViewModel)
            }
            Screen.PROFILE_SETTINGS -> ProfileSettings(it, navViewModel, userViewModel, modifier)

            Screen.POST_DETAIL -> {
                navViewModel.selectedPostId?.let { it1 -> PostDetail(it1, postViewModel, navViewModel, userViewModel, modifier) }
            }

            Screen.PROFILE_OTHER_USER -> {
                navViewModel.selectedUserId?.let { it1 -> ProfileUser(it1, navViewModel, userViewModel, postViewModel, modifier) }
            }
        }
    } ?: run {
        Log.e("MainScreen", "Current user is null")
        Column(
            modifier = modifier.fillMaxSize().background(Color(0xff7d0885).copy(alpha = 0.1f)),
        ) {
            LoadingScreen()
        }

    }

}