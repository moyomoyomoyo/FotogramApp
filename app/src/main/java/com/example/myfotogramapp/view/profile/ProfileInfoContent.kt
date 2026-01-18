package com.example.myfotogramapp.view.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myfotogramapp.navigation.NavigationViewModel
import com.example.myfotogramapp.navigation.Screen
import com.example.myfotogramapp.user.model.UserEntity
import com.example.myfotogramapp.user.viewmodel.UserViewModel
import com.example.myfotogramapp.view.components.FollowButton
import com.example.myfotogramapp.view.components.ProfileImage
import com.example.myfotogramapp.view.components.ViewMode
import com.example.myfotogramapp.view.components.ViewModeToggle

@Composable
fun ProfileInfoContent(
    user: UserEntity?,
    navViewModel: NavigationViewModel,
    userViewModel: UserViewModel,
    viewMode: ViewMode,
    onViewModeChange: (ViewMode) -> Unit,
    modifier: Modifier
) {
    val currentUserId by userViewModel.currentUserId.collectAsState(initial=null)

    Column(
        modifier
            .fillMaxWidth()
            .background(Color(0xFFFFFFFF)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        if(user == null) {
            Text(
                "User not found",
                textAlign = TextAlign.Center,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF7d0885),
                modifier = Modifier.padding(16.dp)
            )
            return@Column
        }

        ProfileImage(user.profilePicture,  isFriend = user.isYourFollowing)
        Spacer(modifier = Modifier.height(15.dp))

        Text(
            "@${user.username}",
            textAlign = TextAlign.Center,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = if (user.isYourFollowing) Color(0xFF7d0885) else Color.Black
        )

        Row(
            modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (user.dateOfBirth.isNotEmpty()) {
                Icon(
                    imageVector = Icons.Filled.DateRange,
                    contentDescription = "Date of Birth",
                    modifier = Modifier.size(20.dp)
                )
                Text(user.dateOfBirth)
            } else {
                Icon(
                    imageVector = Icons.Filled.DateRange,
                    contentDescription = "Date of Birth",
                    tint = Color.Gray,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    " Add your date of birth",
                    color = Color.Gray
                )
            }
        }

        Row(
            modifier.padding(5.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (user.bio.isNotEmpty()) {
                Text(
                    user.bio,
                    modifier = Modifier.padding(horizontal = 16.dp),
                    textAlign = TextAlign.Center
                )
            } else {
                Icon(
                    imageVector = Icons.Filled.Edit,
                    contentDescription = "Edit",
                    tint = Color.Gray,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    "Add your biography",
                    color = Color.Gray
                )
            }
        }

        if(currentUserId == user.id){
            Button(
                onClick = { navViewModel.navigateTo(Screen.PROFILE_SETTINGS) },
                modifier.padding(vertical = 10.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF7d0885),
                    disabledContainerColor = Color.Gray
                )
            ) {
                Text("Edit Profile")
            }
            userViewModel.refreshCurrentUser()
        } else {
            FollowButton(
                user = user,
                userViewModel = userViewModel
            )
        }

        ProfileStats(
            user.postsCount,
            user.followersCount,
            user.followingCount
        )

        HorizontalDivider(
            modifier = Modifier.padding(bottom = 8.dp),
            thickness = 1.dp,
            color = Color(0xFFE0E0E0)
        )

        // Toggle per cambiare vista
        ViewModeToggle(
            currentMode = viewMode,
            onModeChange = onViewModeChange
        )

        HorizontalDivider(
            modifier = Modifier.padding(top = 8.dp),
            thickness = 1.dp,
            color = Color(0xFFE0E0E0)
        )

    }
}