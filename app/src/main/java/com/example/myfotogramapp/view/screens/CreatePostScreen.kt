package com.example.myfotogramapp.view.screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myfotogramapp.application.utilis.normalizeText
import com.example.myfotogramapp.navigation.NavigationViewModel
import com.example.myfotogramapp.navigation.Screen
import com.example.myfotogramapp.post.model.LocationDto
import com.example.myfotogramapp.post.model.NewPost
import com.example.myfotogramapp.post.viewmodel.PostViewModel
import com.example.myfotogramapp.user.viewmodel.UserViewModel
import com.example.myfotogramapp.view.components.Header
import com.example.myfotogramapp.view.components.ImagePicker
import com.example.myfotogramapp.view.components.InputCounter
import com.example.myfotogramapp.view.location.LocationPermission
import com.mapbox.geojson.Point
import kotlinx.coroutines.launch


@SuppressLint("DefaultLocale")
@Composable
fun CreatePostScreen(nav: NavigationViewModel, postViewModel: PostViewModel, userViewModel: UserViewModel) {
    var base64Image by remember { mutableStateOf<String?>(null) }
    var isPosting by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    val captionMaxLength = 100
    var caption by remember { mutableStateOf("") }
    val showErrorCaption = caption.length > captionMaxLength

    // Verifica se tutti i campi sono validi
    val canPost = !base64Image.isNullOrEmpty() && caption.isNotBlank() && !showErrorCaption

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Header(nav)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .background(Color(0xFF7d0885).copy(alpha = 0.1f))
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(10.dp))

            // create post
            Card(
                modifier = Modifier
                    .fillMaxSize(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {

                //aggiunta immagine
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp)
                        .padding(15.dp)
                        .border(1.dp, Color(0xFF7d0885).copy(alpha = 0.3f), RoundedCornerShape(12.dp)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    ImagePicker(
                        isPost = true,
                        onImageSelected = { img ->
                            base64Image = img
                        }
                    )

                    Log.i("Ima", "img: $base64Image")
                }

                Spacer(Modifier.height(16.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        "Caption",
                        style = MaterialTheme.typography.titleMedium,
                        fontSize = 18.sp,
                        color = Color(0xFF7d0885),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 10.dp)
                    )

                    OutlinedTextField(
                        value = caption,
                        onValueChange = { caption = it },
                        placeholder = { Text("Add your caption...") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        maxLines = 5,
                        textStyle = TextStyle(fontSize = 16.sp),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF7d0885),
                            focusedLabelColor = Color(0xFF7d0885),
                            cursorColor = Color(0xFF7d0885)
                        ),
                    )

                    InputCounter(
                        caption.length,
                        captionMaxLength,
                        modifier = Modifier.align(Alignment.End)
                    )

                    if (showErrorCaption) {
                        val errorMessage = when {
                            caption.length > captionMaxLength -> "Caption must be less than $captionMaxLength characters"
                            else -> ""
                        }

                        Text(
                            text = errorMessage,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .padding(bottom = 8.dp)
                        )
                    }
                }
                Spacer(Modifier.height(16.dp))
                val selectedPoint = locationAdd()

                Spacer(Modifier.height(24.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp)
                ) {
                    val scope = rememberCoroutineScope()
                    // Pulsante Post
                    Button(
                        onClick = {
                            if (canPost && !isPosting) {
                                isPosting = true

                                val newPost = NewPost(
                                    contentText = normalizeText(caption),
                                    contentPicture = base64Image ?: "",
                                    location = selectedPoint?.let { point ->
                                        LocationDto(
                                            longitude = point.longitude(),
                                            latitude = point.latitude()
                                        )
                                    }
                                )

                                scope.launch {
                                    try {
                                        postViewModel.createPost(newPost)
                                        userViewModel.refreshCurrentUser()
                                        Log.i("CreatePostScreen", "Utente refreshato dopo creazione post")
                                    } catch (e: Exception) {
                                        Log.e("CreatePostScreen", "Errore: ${e.message}")
                                    } finally {
                                        isPosting = false
                                        nav.navigateTo(Screen.PROFILE)
                                    }
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        enabled = canPost && !isPosting,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF7d0885),
                            contentColor = Color.White,
                            disabledContainerColor = Color(0xFF7d0885).copy(alpha = 0.5f),
                            disabledContentColor = Color.White.copy(alpha = 0.5f)
                        )
                    ) {
                        if (isPosting) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = Color(0xff7d0885)
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Filled.Send,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(Modifier.width(8.dp))
                            Text("Share Post", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(Modifier.height(16.dp))

                    // Pulsante Annulla
                    OutlinedButton(
                        onClick = { nav.navigateBack() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .border(1.dp, Color(0xFF7d0885), RoundedCornerShape(12.dp)),
                        shape = RoundedCornerShape(12.dp),
                    ) {
                        Text("Cancel", fontSize = 16.sp, color = Color(0xFF7d0885))
                    }

                    Spacer(Modifier.height(32.dp))
                }

            }
            Spacer(Modifier.height(24.dp))
        }
    }
}


@SuppressLint("DefaultLocale")
@Composable
fun locationAdd(): Point? {
    var selectedPoint by remember { mutableStateOf<Point?>(null) }
    var showLocationDialog by remember { mutableStateOf(false) }

    // Card Location
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp)
            .border(1.dp, Color(0xFF7d0885), RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(
            containerColor = if (selectedPoint != null)
                MaterialTheme.colorScheme.primaryContainer
            else
                MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        "Location",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color(0xFF7d0885),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    when (val point = selectedPoint) {
                        null -> {
                            Spacer(Modifier.height(4.dp))
                            Text("Optional", color = Color.Gray)
                        }

                        else -> {
                            Spacer(Modifier.height(4.dp))
                            Text(
                                "Lat: ${String.format("%.4f", point.latitude())}, " +
                                        "Lon: ${String.format("%.4f", point.longitude())}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }

                }
                FilledTonalButton(
                    onClick = {
                        if (selectedPoint != null) {
                            // Quando c'è una posizione selezionata, la rimuovi
                            selectedPoint = null
                            showLocationDialog = false
                        } else {
                            // Se non c'è una posizione, apri il dialogo
                            showLocationDialog = true
                        }
                    },
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = if (selectedPoint != null)
                            Color(0xFF7d0885)   // colore personalizzato quando è "Added"
                        else
                            Color(0xFF7d0885).copy(alpha = 0.1f),
                        contentColor = if (selectedPoint != null)
                            Color.White         // testo e icona bianchi
                        else
                            MaterialTheme.colorScheme.onSecondaryContainer
                    )
                ) {
                    Icon(
                        imageVector = if (selectedPoint != null) Icons.Filled.Close else Icons.Filled.LocationOn,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = if (selectedPoint != null) Color.White else Color(0xFF7d0885)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        if (selectedPoint != null) "Remove" else "Add",
                        color = if (selectedPoint != null) Color.White else Color(0xFF7d0885)
                    )
                }

            }
        }


    }
    if (showLocationDialog) {
        LocationPermission(
            onDismiss = { showLocationDialog = false },
            onLocationSelected = { point ->
                selectedPoint = point
            }
        )
    }
    return selectedPoint
}
