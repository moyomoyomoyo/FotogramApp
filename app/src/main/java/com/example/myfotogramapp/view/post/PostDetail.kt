package com.example.myfotogramapp.view.post

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.myfotogramapp.navigation.NavigationViewModel
import com.example.myfotogramapp.post.model.PostEntity
import com.example.myfotogramapp.post.viewmodel.PostViewModel
import com.example.myfotogramapp.user.model.UserEntity
import com.example.myfotogramapp.user.viewmodel.UserViewModel
import com.example.myfotogramapp.view.components.Header
import com.example.myfotogramapp.view.components.LoadingScreen

@Composable
fun PostDetail(postId: Int, postViewModel: PostViewModel, navViewModel: NavigationViewModel, userViewModel: UserViewModel, modifier: Modifier) {
    var post by remember { mutableStateOf<PostEntity?>(null) }
    var selectedUser by remember { mutableStateOf<UserEntity?>(null) }
    var isLoadingPost by remember { mutableStateOf(true) }
    var isLoadingUser by remember { mutableStateOf(false) }

    // Carica il post
    LaunchedEffect(postId) {
        Log.d("PostDetail", "Loading postId: $postId")
        isLoadingPost = true
        post = postViewModel.getPostById(postId)
        isLoadingPost = false
        Log.d("PostDetail", "Post loaded: $post")
    }

    // Carica l'utente quando il post è pronto
    LaunchedEffect(post) {
        Log.d("PostDetail", "LaunchedEffect post triggered, post = $post")
        post?.let {
            Log.i("PostDetail", "Loading authorId: ${it.authorId}")
            isLoadingUser = true
            selectedUser = userViewModel.getUserById(it.authorId)
            isLoadingUser = false
            Log.i("PostDetail", "User loaded: ${selectedUser?.id}")
        }
    }
    // Aggiungi un flag per tenere traccia del caricamento
    val isLoading = post == null || selectedUser == null

    // Se il post è in fase di caricamento, mostriamo un indicatore
    if (isLoading) {
        LoadingScreen()
    } else {
        // Header
        Header(navViewModel, selectedUser)

        // Quando il post è caricato, mostriamo il contenuto
        post?.let { element ->
            selectedUser?.let { user ->
                Column(modifier = Modifier.fillMaxSize()) {
                    PostDetailContent(navViewModel, element, user, userViewModel, postViewModel, modifier)
                }
            }
        } ?: run {
            // Se il post non è trovato, mostriamo il messaggio "Post not found"
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Post not found",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color(0xff7d0885),
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}