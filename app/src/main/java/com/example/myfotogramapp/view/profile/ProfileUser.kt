package com.example.myfotogramapp.view.profile


import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.myfotogramapp.navigation.NavigationViewModel
import com.example.myfotogramapp.post.model.PostEntity
import com.example.myfotogramapp.post.viewmodel.PostViewModel
import com.example.myfotogramapp.user.viewmodel.UserViewModel
import com.example.myfotogramapp.view.components.LoadingScreen

@Composable
fun ProfileUser(
    accountId: Int,
    navViewModel: NavigationViewModel,
    userViewModel: UserViewModel,
    postViewModel: PostViewModel,
    modifier: Modifier

) {
    val userFollow by userViewModel.userFollowState.collectAsState()
    var allPosts by remember { mutableStateOf<List<PostEntity>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    // recupero utente
    LaunchedEffect(accountId) {
        isLoading = true
        userViewModel.loadUser(accountId)
    }

    // recupero i post dell'utente recuperato
    LaunchedEffect(userFollow?.id) {
        userFollow?.let { user ->
            allPosts = postViewModel.getPostsByAuthor(user.id)
            isLoading = false
        }
    }


    if (isLoading) {
        LoadingScreen()
    } else { ProfileScreen( user = userFollow, posts = allPosts, navViewModel = navViewModel, userViewModel = userViewModel, postViewModel = postViewModel, modifier = modifier ) }

}