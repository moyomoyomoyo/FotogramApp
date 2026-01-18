package com.example.myfotogramapp.view.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowLeft
import androidx.compose.material.icons.outlined.KeyboardArrowRight
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myfotogramapp.navigation.NavigationViewModel
import com.example.myfotogramapp.navigation.Screen
import com.example.myfotogramapp.user.model.UserEntity

@Composable
fun Header(nav: NavigationViewModel, user: UserEntity? = null) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(3.dp)
            .height(35.dp)
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                when (nav.currentScreen) {
                    Screen.PROFILE -> {
                        IconButton(
                            onClick = { nav.navigateTo(Screen.FEED) },

                            ) {
                            Icon(
                                imageVector = Icons.Outlined.KeyboardArrowLeft,
                                contentDescription = "Back",
                                modifier = Modifier
                                    .size(32.dp)
                            )
                        }

                        Text(
                            text = user?.username ?: "Profile",
                            style = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.Bold)
                        )

                        IconButton(
                            onClick = { nav.navigateTo(Screen.PROFILE_SETTINGS) },

                            ) {
                            Icon(
                                imageVector = Icons.Outlined.MoreVert,
                                contentDescription = "Settings",
                                modifier = Modifier
                                    .size(30.dp)
                            )
                        }
                    }

                    Screen.PROFILE_OTHER_USER -> {
                        IconButton(
                            onClick = { nav.navigateTo(Screen.FEED) },
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.KeyboardArrowLeft,
                                contentDescription = "Back",
                                modifier = Modifier
                                    .size(32.dp),
                                tint = if(user?.isYourFollowing == true) Color(0xFF7D0885) else Color.Black
                            )
                        }

                        Text(
                            text = user?.username ?: "Uknown",
                            style = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.Bold),
                            color = if(user?.isYourFollowing == true) Color(0xFF7D0885) else Color.Black
                        )

                        Box(modifier = Modifier.size(32.dp)) {}
                    }

                    Screen.FEED -> Text(
                        text = "Feed",
                        style = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.Bold),
                        modifier = Modifier
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )

                    Screen.CREATE_POST -> {
                        Box(modifier = Modifier.size(32.dp)) {}

                        Text(
                            text = "Create",
                            style = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.Bold)
                        )

                        IconButton(
                            onClick = { nav.navigateTo(Screen.FEED) },

                            ) {
                            Icon(
                                imageVector = Icons.Outlined.KeyboardArrowRight,
                                contentDescription = "Back",
                                modifier = Modifier
                                    .size(32.dp)
                            )
                        }
                    }

                    Screen.PROFILE_SETTINGS -> {
                        IconButton(
                            onClick = { nav.navigateTo(Screen.PROFILE) },
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.KeyboardArrowLeft,
                                contentDescription = "Back",
                                modifier = Modifier
                                    .size(32.dp)
                            )
                        }

                        Text(
                            text = "Settings",
                            style = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.Bold)
                        )

                        Box(modifier = Modifier.size(32.dp)) {}
                    }

                    Screen.POST_DETAIL -> {
                        IconButton(
                            onClick = { nav.navigateBack() },
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.KeyboardArrowLeft,
                                contentDescription = "Back",
                                modifier = Modifier
                                    .size(32.dp)
                            )
                        }
                        if (user != null) {
                            Text(
                                text = "@${user.username}",
                                style = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.Bold)
                            )
                        }

                        Box(modifier = Modifier.size(32.dp)) {}
                    }

                    Screen.SAVED_POSTS -> {
                        Box(modifier = Modifier.size(32.dp)) {}

                        Text(
                            text = "Saved Posts",
                            style = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.Bold)
                        )

                        Box(modifier = Modifier.size(32.dp)) {}
                    }
                }




//                }
            }
        }
    }
}