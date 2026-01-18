package com.example.myfotogramapp

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.example.myfotogramapp.application.AppViewModel
import com.example.myfotogramapp.application.AppViewModelFactory
import com.example.myfotogramapp.auth.AuthManager
import com.example.myfotogramapp.auth.sessionStore
import com.example.myfotogramapp.navigation.NavigationViewModel
import com.example.myfotogramapp.network.HttpClientProvider
import com.example.myfotogramapp.post.repository.PostRepository
import com.example.myfotogramapp.post.viewmodel.PostViewModel
import com.example.myfotogramapp.post.viewmodel.PostViewModelFactory
import com.example.myfotogramapp.settings.SettingsRepository
import com.example.myfotogramapp.settings.settingsStore
import com.example.myfotogramapp.ui.theme.MyFotogramAppTheme
import com.example.myfotogramapp.user.model.UserInfoUpdateDto
import com.example.myfotogramapp.user.repository.UserRepository
import com.example.myfotogramapp.user.viewmodel.UserViewModel
import com.example.myfotogramapp.user.viewmodel.UserViewModelFactory
import com.example.myfotogramapp.view.components.LoadingScreen
import com.example.myfotogramapp.view.components.MainScreen
import com.example.myfotogramapp.view.components.NavBar
import com.example.myfotogramapp.view.screens.LoginScreen

class MainActivity : ComponentActivity() {

    private val navViewModel: NavigationViewModel by viewModels()

    private lateinit var userViewModel: UserViewModel
    private lateinit var postViewModel: PostViewModel
    private lateinit var appViewModel: AppViewModel
    private lateinit var authManager: AuthManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. AuthManager (unifica SessionManager + SessionRepository)
        authManager = AuthManager(applicationContext)

        // 2. HttpClient con sessionId automatico
        val httpClient = HttpClientProvider.create(authManager)

        // 3. Repository puliti
        val userRepository = UserRepository(applicationContext, httpClient)
        val postRepository = PostRepository(applicationContext, httpClient)

        val settingsRepository = SettingsRepository(applicationContext)
        appViewModel = ViewModelProvider(
            this,
            AppViewModelFactory(settingsRepository, authManager)
        )[AppViewModel::class.java]


        // 4. ViewModel
        userViewModel = ViewModelProvider(
            this,
            UserViewModelFactory(userRepository, authManager)
        )[UserViewModel::class.java]

        postViewModel = ViewModelProvider(
            this,
            PostViewModelFactory(postRepository)
        )[PostViewModel::class.java]

        appViewModel = ViewModelProvider(
            this,
            AppViewModelFactory(settingsRepository, authManager)
        )[AppViewModel::class.java]

        enableEdgeToEdge()

        setContent {
            val sessionId by authManager.sessionId.collectAsState()
//            val userId by authManager.userId.collectAsState()
            val sessionLoaded by authManager.sessionLoaded.collectAsState()
            val firstLaunch by appViewModel.isFirstLaunch.collectAsState()

            MyFotogramAppTheme {
                when {
                    !sessionLoaded -> { LoadingScreen() }

                    sessionId == null -> {
                        // non ho la sessione, devo creare un utente
                        LaunchedEffect(Unit) {
                            userViewModel.createUser()
                        }
                        LoadingScreen()
                    }

                    firstLaunch && sessionId != null -> {
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
                        NavBar(
                            navViewModel = navViewModel,
                            userViewModel = userViewModel,
                            postViewModel = postViewModel,
                            modifier = Modifier
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun Greeting(navViewModel: NavigationViewModel, userViewModel: UserViewModel, postViewModel: PostViewModel, modifier: Modifier = Modifier) {
    MainScreen(navViewModel, userViewModel, postViewModel, modifier = Modifier)
}

//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    MyFotogramAppTheme {
//        Greeting("Android")
//    }
//}