package com.example.myfotogramapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import com.example.myfotogramapp.application.AppViewModel
import com.example.myfotogramapp.application.AppViewModelFactory
import com.example.myfotogramapp.auth.AuthManager
import com.example.myfotogramapp.feed.FeedViewModel
import com.example.myfotogramapp.feed.FeedViewModelFactory
import com.example.myfotogramapp.navigation.NavigationViewModel
import com.example.myfotogramapp.network.HttpClientProvider
import com.example.myfotogramapp.post.repository.PostRepository
import com.example.myfotogramapp.post.viewmodel.PostViewModel
import com.example.myfotogramapp.post.viewmodel.PostViewModelFactory
import com.example.myfotogramapp.settings.SettingsRepository
import com.example.myfotogramapp.ui.theme.MyFotogramAppTheme
import com.example.myfotogramapp.user.repository.UserRepository
import com.example.myfotogramapp.user.viewmodel.UserViewModel
import com.example.myfotogramapp.user.viewmodel.UserViewModelFactory
import com.example.myfotogramapp.view.components.LoadingScreen
import com.example.myfotogramapp.view.screens.MainScreen
import com.example.myfotogramapp.view.components.NavBar
import com.example.myfotogramapp.view.screens.LoginScreen

class MainActivity : ComponentActivity() {

    private val navViewModel: NavigationViewModel by viewModels()

    private lateinit var userViewModel: UserViewModel
    private lateinit var postViewModel: PostViewModel
    private lateinit var feedViewModel: FeedViewModel
    private lateinit var appViewModel: AppViewModel
    private lateinit var authManager: AuthManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // auth manager inizializzazione
        authManager = AuthManager(applicationContext)

        // creazione httpclient
        val httpClient = HttpClientProvider.create(authManager)

        // repository
        val userRepository = UserRepository(applicationContext, httpClient)
        val postRepository = PostRepository(applicationContext, httpClient)

        val settingsRepository = SettingsRepository(applicationContext)
        appViewModel = ViewModelProvider(
            this,
            AppViewModelFactory(settingsRepository, authManager)
        )[AppViewModel::class.java]

        // viewModel
        userViewModel = ViewModelProvider(
            this,
            UserViewModelFactory(userRepository, authManager)
        )[UserViewModel::class.java]

        postViewModel = ViewModelProvider(
            this,
            PostViewModelFactory(postRepository,authManager)
        )[PostViewModel::class.java]

        feedViewModel = ViewModelProvider(
            this,
            FeedViewModelFactory(postRepository, authManager)
        )[FeedViewModel::class.java]

        appViewModel = ViewModelProvider(
            this,
            AppViewModelFactory(settingsRepository, authManager)
        )[AppViewModel::class.java]

        enableEdgeToEdge()

        setContent {
            val sessionId by authManager.sessionId.collectAsState()
            val userId by authManager.userId.collectAsState()
            val sessionLoaded by authManager.sessionLoaded.collectAsState()
            val firstLaunch by appViewModel.isFirstLaunch.collectAsState()

            LaunchedEffect(sessionLoaded, sessionId, userId, firstLaunch) {
                Log.i("MainActivity", "📱 STATE: sessionLoaded=$sessionLoaded, sessionId=$sessionId, userId=$userId, firstLaunch=$firstLaunch")
            }

            MyFotogramAppTheme {
                when {
                    !sessionLoaded -> { LoadingScreen() }

                    sessionId == null -> {
                        LaunchedEffect(Unit) {
                            userViewModel.createUser()
                        }
                        LoadingScreen()
                    }

                    firstLaunch && sessionId != null && userId != null-> {
                        Log.i("MainActivity", "firstLaunch= é true come??????????????")
                        LoginScreen(
                            onLoginCompleted = { username, picture ->
                                val success = userViewModel.updateUserInfo(username = username, bio = "", dateOfBirth = "", picture = picture)
                                if (success) {
                                    appViewModel.setFirstLaunchDone()
                                }
                            }
                        )
                    }

                    else -> {
                        Log.i("MainActivity", "🔵 Caso 4: Mostra NavBar")
                        NavBar(
                            navViewModel = navViewModel,
                            userViewModel = userViewModel,
                            postViewModel = postViewModel,
                            feedViewModel = feedViewModel,
                            modifier = Modifier
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun Greeting(navViewModel: NavigationViewModel, userViewModel: UserViewModel, postViewModel: PostViewModel, feedViewModel: FeedViewModel, modifier: Modifier = Modifier) {
    MainScreen(navViewModel, userViewModel, postViewModel, feedViewModel, modifier = Modifier)
}

//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    MyFotogramAppTheme {
//        Greeting("Android")
//    }
//}