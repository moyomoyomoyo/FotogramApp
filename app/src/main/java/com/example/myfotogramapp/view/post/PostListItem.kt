package com.example.myfotogramapp.view.post


import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myfotogramapp.navigation.NavigationViewModel
import com.example.myfotogramapp.post.model.PostEntity
import com.example.myfotogramapp.post.view.PostDescription
import com.example.myfotogramapp.post.viewmodel.PostViewModel
import com.example.myfotogramapp.user.model.UserEntity
import com.example.myfotogramapp.user.viewmodel.UserViewModel
import com.example.myfotogramapp.view.components.ProfileImage

@Composable
fun PostListItem(
    post: PostEntity,
    navViewModel: NavigationViewModel,
    user: UserEntity,
    userViewModel: UserViewModel,
    postViewModel: PostViewModel,
    modifier: Modifier
) {

    val currentUserId by userViewModel.currentUserId.collectAsState(initial=null)

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 8.dp)
                .border(
                    width = if (user.isYourFollowing) 4.dp else 0.dp,
                    color = if (user.isYourFollowing) Color(0xFF7d0885) else Color.Transparent,
                    shape = RoundedCornerShape(8.dp)
                ),
            elevation = CardDefaults.cardElevation(defaultElevation = 7.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(13.dp)
            ) {
                // Immagine del post se presente
                Row(
                    modifier = Modifier.clickable {if(user.id == currentUserId) navViewModel.navigateToProfile(user.id) else navViewModel.navigateToOtherProfile(user.id) }, //.padding(13.dp).
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ProfileImage(user.profilePicture, 40.dp, user.isYourFollowing)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = user.username,
                        style = MaterialTheme.typography.titleMedium,
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (user.isYourFollowing) Color(0xFF7d0885) else Color.Black
                    )
                }
                Spacer(modifier = Modifier.height(18.dp))

                Spacer(modifier = Modifier.height(12.dp))
                ShowPostPicture(post.contentPicture)
                Spacer(modifier = Modifier.height(12.dp))

                Column(modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp)
                ) {
                    PostDescription(post.contentText, post)
                }
            }
        }
    }
    Spacer(modifier = Modifier.height(8.dp))

}





