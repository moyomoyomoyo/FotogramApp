package com.example.myfotogramapp.view.post


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.myfotogramapp.view.profile.ProfileInfoContent

@Composable
fun PostList(
    user: UserEntity,
    navViewModel: NavigationViewModel,
    posts: List<PostEntity?> = emptyList(),
    userViewModel: UserViewModel,
    viewMode: ViewMode,
    postViewModel: PostViewModel,
    modifier: Modifier,
    onViewModeChange: (ViewMode) -> Unit,

    ) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        contentPadding = PaddingValues(top = 0.dp, start = 4.dp, end = 4.dp, bottom = 4.dp)
    ) {

        item {
            ProfileInfoContent(
                user = user,
                navViewModel = navViewModel,
                userViewModel = userViewModel,
                viewMode = viewMode,
                onViewModeChange = onViewModeChange,
                modifier = modifier
            )
        }

        if(posts.isEmpty()) {

            item{
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 50.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "No posts to display",
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        textAlign = TextAlign.Center,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF7d0885)
                    )
                }
            }

        } else {
            // Post in formato lista
            items(posts.filterNotNull()) { post ->
                PostListItem(
                    post = post,
                    navViewModel = navViewModel,
                    user = user,
                    userViewModel = userViewModel,
                    postViewModel = postViewModel,
                    modifier
                )
            }
        }
    }
}