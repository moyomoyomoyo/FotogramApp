package com.example.myfotogramapp.view.components


import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.myfotogramapp.user.model.UserEntity
import com.example.myfotogramapp.user.viewmodel.UserViewModel
import kotlinx.coroutines.launch

@Composable
fun FollowButton(
    user: UserEntity,
    userViewModel: UserViewModel
) {

    var isFollowing by remember { mutableStateOf(user.isYourFollowing) }
    var isLoading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(user.id, user.isYourFollowing) {
        isFollowing = user.isYourFollowing
        Log.i("FollowButton", "Sync: user ${user.id}, isFollowing=$isFollowing")
    }

    Button(
        onClick = {
            scope.launch {
                isLoading = true
                try {
                    if (isFollowing) {
                        userViewModel.unfollow(user.id)
                        Log.i("FollowButton", "Unfollowing user with ID: ${user.id}")
                    } else {
                        userViewModel.follow(user.id)
                        Log.i("FollowButton", "Following user with ID: ${user.id}")
                    }
                    isFollowing = !isFollowing

                } catch (e: Exception) {
                    Log.e("FollowButton", "Error follow/unfollow: ${e.message}")
                } finally {
                    isLoading = false
                }
            }
        },
        enabled = !isLoading,
        modifier = Modifier.padding(15.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isFollowing) Color(0xFF7d0885).copy(alpha = 0.5f) else Color(0xFF7d0885)
        )
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                color = Color.White,
                strokeWidth = 2.dp
            )
        } else {
            Text(
                text = if (isFollowing) "Following" else "Follow",
                color =if (isFollowing) Color.White.copy(alpha= 0.5f) else Color.White
            )
        }
    }
}