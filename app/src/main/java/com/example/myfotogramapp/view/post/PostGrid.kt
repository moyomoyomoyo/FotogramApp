package com.example.myfotogramapp.view.post


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
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
import com.example.myfotogramapp.user.model.UserEntity
import com.example.myfotogramapp.user.viewmodel.UserViewModel
import com.example.myfotogramapp.view.components.ViewMode
import com.example.myfotogramapp.view.profile.ProfileInfoContent

@Composable
fun PostGrid(
    user: UserEntity,
    navViewModel: NavigationViewModel,
    posts: List<PostEntity?>,
    userViewModel: UserViewModel,
    viewMode: ViewMode,
    modifier: Modifier,
    onViewModeChange: (ViewMode) -> Unit
) {

    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier
            .fillMaxSize(),
        contentPadding = PaddingValues(top = 0.dp, start = 4.dp, end = 4.dp, bottom = 4.dp)
    ) {

        // Profilo info (senza Header che ora è fisso) che occupa tutte le colonne
        item(span = { GridItemSpan(3) }) {
            ProfileInfoContent(
                user = user,
                navViewModel = navViewModel,
                userViewModel = userViewModel,
                viewMode = viewMode,
                onViewModeChange = onViewModeChange,
                modifier = modifier
            )
        }

        if(posts.isEmpty()){
            // Messaggio di nessun post
            item(span = { GridItemSpan(3) }) {
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
            // Post in formato griglia
            items(posts.filterNotNull()) { post ->
                PostGridItem(
                    post = post,
                    navViewModel = navViewModel
                )
            }
        }

    }
}