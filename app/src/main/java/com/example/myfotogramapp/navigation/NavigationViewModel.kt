package com.example.myfotogramapp.navigation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class NavigationViewModel : ViewModel() {
    var currentScreen by mutableStateOf(Screen.FEED)
        private set

    private var pastScreen by mutableStateOf(Screen.FEED)
        private set

    var selectedPostId by mutableStateOf<Int?>(null)
        private set

    var selectedUserId by mutableStateOf<Int?>(null)
        private set


    fun navigateTo(screen: Screen) {
        currentScreen = screen
    }

    fun navigateBack() {
        currentScreen = pastScreen
    }

    fun navigateToPostDetail(postId: Int){
        selectedPostId = postId
        pastScreen = currentScreen
        currentScreen = Screen.POST_DETAIL
    }

    fun navigateToProfile(userId: Int){
        selectedUserId = userId
        pastScreen = currentScreen
        currentScreen = Screen.PROFILE
    }

    fun navigateToOtherProfile(userId: Int){
        selectedUserId = userId
        pastScreen = currentScreen
        currentScreen = Screen.PROFILE_OTHER_USER
    }

}