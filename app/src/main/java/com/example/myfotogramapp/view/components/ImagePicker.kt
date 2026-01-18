package com.example.myfotogramapp.view.components

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import java.io.ByteArrayOutputStream
import androidx.core.graphics.scale
import com.example.myfotogramapp.R

@Composable
fun ImagePicker(
    modifier: Modifier = Modifier,
    isPost: Boolean = false,
    initialImage: String = "",
    onImageSelected: (String?) -> Unit
) {
    val context = LocalContext.current
    var base64String by remember { mutableStateOf(initialImage) }
    var errorMsg by remember { mutableStateOf<String?>(null) }
    var showDialog by remember { mutableStateOf(false) }

    val bitmap = try {
        if(initialImage.isEmpty()) null
        else viewPicture(initialImage).asImageBitmap()
    } catch (e: Exception) {
        null
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            try {
                val result = convertImageToBase64(context, uri)
                Log.i("ImagePicker", "Image selection result: $result")

                if (result != null) {
                    // ✔ immagine valida → aggiorna
                    base64String = result
                    onImageSelected(result)
                } else {
                    showDialog = true
                }

            } catch (e: Exception) {
                errorMsg = "Error: ${e.message}"
            }
        }
    }

    if(showDialog) {
        ShowLargeDialog(
            onDismiss = { showDialog = false }
        )
    }

    Card(
        modifier = modifier
            .size(if (isPost) 450.dp else 120.dp)
            .clickable { launcher.launch("image/*") },
        shape = RoundedCornerShape(if (isPost) 0.dp else 100.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        when(bitmap) {
            null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFFEAEAEA)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PhotoCamera,
                        contentDescription = "Add image",
                        tint =Color(0xFF7d0885).copy(alpha = 0.5f),
                        modifier = Modifier.size(48.dp)
                    )
                }
            }

            else -> {

                Image(
                    bitmap = bitmap,
                    contentDescription = "Selected Image",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}

@Composable
fun ShowLargeDialog(
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("OK", color = Color(0xFF7d0885))
            }
        },
        title = {
            Text("Image too large")
        },
        text = {
            Text("The selected image exceeds the maximum allowed size.")
        },
        icon = {
            Image(
                painter = painterResource(R.drawable.warning_purple),
                contentDescription = "Warning",
                modifier = Modifier.size(50.dp)
            )
        }
    )
}


/* ---------------------- FUNZIONI DI SUPPORTO ---------------------- */

/* Converts an image into a base64, with compression if needed */
fun convertImageToBase64(
    context: Context,
    uri: Uri
): String? {

    val maxSize = 80_000

    // Load image from the URI
    val stream = context.contentResolver.openInputStream(uri)
    var bitmap = BitmapFactory.decodeStream(stream)
    val out = ByteArrayOutputStream()
    stream?.close()

//    // Log original size without compression (You can delete this)
//    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
//    val originalBase64 = Base64.encodeToString(out.toByteArray(), Base64.NO_WRAP)
//    Log.d("ImagePicker", "Original base64 size: ${originalBase64.length} chars")
//    out.reset()

    // Transform into a Base64 image (with compression)
    val scale = minOf(600f / bitmap.width, 600f / bitmap.height)
    bitmap = bitmap.scale((bitmap.width * scale).toInt(), (bitmap.height * scale).toInt())

    bitmap.compress(Bitmap.CompressFormat.JPEG, 70, out)
    val base64 = Base64.encodeToString(out.toByteArray(), Base64.NO_WRAP)

    Log.i("ImagePicker", "Compressed base64 size: ${base64.length} chars")

    if(base64.length > maxSize) {
        return null
    }
    return base64
}
