package com.example.myfotogramapp.view.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.myfotogramapp.application.utilis.isValidUsername
import com.example.myfotogramapp.view.components.ImagePicker
import com.example.myfotogramapp.view.components.InputCounter
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    onLoginCompleted: suspend (String, String) -> Unit
) {
    Log.i("LoadingScreen", "MOSTRATO!") // ← Verifica che venga chiamato

    var username by remember { mutableStateOf("") }
    var profilePic by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    val usernameMaxLength = 15

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFe5d3e5))
            .padding(bottom = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(32.dp))

                // titolo
                Text(
                    "FOTOGRAM",
                    style = MaterialTheme.typography.headlineMedium,
                    fontSize = 50.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF7d0885)
                )

                Spacer(Modifier.height(24.dp))

                // immagine profilo
                Text(
                    "Upload your profile picture",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.DarkGray,
                    fontWeight = FontWeight.Medium
                )

                Spacer(Modifier.height(16.dp))

                Box(
                    modifier = Modifier.size(150.dp),
                    contentAlignment = Alignment.Center
                ) {

                    ImagePicker(
                        modifier = Modifier.size(150.dp),
                        initialImage = profilePic,
                        onImageSelected = { base64 ->
                            if (base64 != null) {
                                profilePic = base64
                            }
                        }
                    )
                }

                Spacer(Modifier.height(8.dp))

                Text(
                    "Tap to change",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }

            // text field username
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Username") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Person,
                        contentDescription = null,
                        tint = Color(0xFF7d0885)
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF7d0885),
                    focusedLabelColor = Color(0xFF7d0885),
                    cursorColor = Color(0xFF7d0885)
                ),

                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
            InputCounter(username.length, usernameMaxLength, modifier = Modifier.padding(horizontal = 16.dp))

            // messaggi di errore se non si inserisce username e/o foto profilo o il nome non é valido
            if (showError) {
                Spacer(Modifier.height(8.dp))

                val errorMessage = when {
                    username.length > usernameMaxLength -> "Username must be less than $usernameMaxLength characters"

                    username.isBlank() ->
                        "Add a username to sign up"

                    profilePic.isBlank() ->
                        "Add a profile picture to sign up"

                    !isValidUsername(username) -> "Username can contain only letters, numbers and _"

                    else -> ""
                }

                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            Spacer(Modifier.height(24.dp))


            // pulsante di sign up
            Button(
                onClick = {
                    showError = username.isBlank() ||
                            !isValidUsername(username) ||
                            profilePic.isBlank() ||
                            username.length > usernameMaxLength
                    if (!showError) {
                        scope.launch{
                            onLoginCompleted(username, profilePic)
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF7d0885),
                    disabledContainerColor = Color.Gray
                )
            ) {
                Text("Sign Up", fontWeight = FontWeight.SemiBold)
            }
            Spacer(Modifier.height(54.dp))
        }

    }
}