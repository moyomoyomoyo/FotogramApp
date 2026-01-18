package com.example.myfotogramapp.view.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.myfotogramapp.navigation.NavigationViewModel
import com.example.myfotogramapp.navigation.Screen
import com.example.myfotogramapp.post.viewmodel.PostViewModel
import com.example.myfotogramapp.user.viewmodel.UserViewModel
import androidx.compose.runtime.collectAsState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navViewModel: NavigationViewModel, userViewModel: UserViewModel, postViewModel: PostViewModel, modifier: Modifier) {

    val currentUser = userViewModel.currentUser.collectAsState().value
    if(currentUser == null) {
        LoadingScreen()
        return
    }
        when(navViewModel.currentScreen) {
            Screen.FEED -> Text("Feed")
            Screen.PROFILE -> Text("Profile")
            Screen.CREATE_POST -> Text("Create Post")
//            Screen.PROFILE_SETTINGS -> Text("Profile Settings")
            Screen.POST_DETAIL -> Text("Post Detail")
//            Screen.PROFILE_OTHER_USER -> Text("Other User Profile")
            else -> { Text("tua mamma")}
        }

}