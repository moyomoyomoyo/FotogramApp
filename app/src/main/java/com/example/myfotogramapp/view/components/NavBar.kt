package com.example.myfotogramapp.view.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddBox
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.AddBox
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.myfotogramapp.Greeting
import com.example.myfotogramapp.post.viewmodel.PostViewModel
import com.example.myfotogramapp.navigation.NavigationViewModel
import com.example.myfotogramapp.navigation.Screen
import com.example.myfotogramapp.user.viewmodel.UserViewModel

data class BottomNavItem(
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

@Composable
fun NavBar(navViewModel: NavigationViewModel, userViewModel: UserViewModel, postViewModel: PostViewModel, modifier: Modifier) {

    val items = listOf(
        BottomNavItem("Create", Icons.Filled.AddBox, Icons.Outlined.AddBox),
        BottomNavItem("Feed", Icons.Filled.Home, Icons.Outlined.Home),
        BottomNavItem("Profile", Icons.Filled.Person, Icons.Outlined.Person)
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                items.forEachIndexed { index, item ->

                    val screenForIndex = when (index) {
                        0 -> Screen.CREATE_POST
                        1 -> Screen.FEED
                        2 -> Screen.PROFILE
                        else -> Screen.FEED
                    }

                    NavigationBarItem(
                        selected = navViewModel.currentScreen == screenForIndex,
                        onClick = { navViewModel.navigateTo(screenForIndex) },
                        label = { Text(item.label) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color(0xFF7d0885),
                            indicatorColor = Color(0xFF7d0885).copy(alpha = 0.1f),
                        ),
                        icon = {
                            Icon(
                                imageVector = if (navViewModel.currentScreen == screenForIndex)
                                    item.selectedIcon
                                else
                                    item.unselectedIcon,
                                contentDescription = item.label
                            )
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            Greeting(navViewModel, userViewModel, postViewModel, modifier = Modifier.padding(innerPadding))
        }
    }
}