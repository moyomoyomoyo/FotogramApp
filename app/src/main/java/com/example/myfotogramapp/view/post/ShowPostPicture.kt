package com.example.myfotogramapp.view.post


import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.myfotogramapp.R
import com.example.myfotogramapp.view.components.viewPicture

@Composable
fun ShowPostPicture(
    picture: String
) {
    var showImageDialog by remember { mutableStateOf(false) }
    val maxSize = 80_000

    val bitmap = try {
        if(picture.isEmpty()) null
        else viewPicture(picture).asImageBitmap()
    } catch (e: Exception) {
        null
    }
    if (picture.isEmpty() || bitmap == null) { // Immagine non valida → placeholder grigio
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.LightGray),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally)
            {
                Image(
                    painter = painterResource(R.drawable.no_photo) ,
                    contentDescription = "Placeholder",
                    modifier = Modifier.size(100.dp),
                    contentScale = ContentScale.Crop
                )
                Text(text = "Picture not valid", color = Color.DarkGray)
            }
        }
        Log.i("showPostPicture", "picture empty or bitmap null")
    } else if(picture.length > maxSize) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.LightGray),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally)
            {
                Image(
                    painter = painterResource(R.drawable.warning_purple),
                    contentDescription = "Warning",
                    modifier = Modifier.size(50.dp)
                )
                Text(text = "Picture too large", color = Color(0xFF7d0885))
            }
        }

    }

    else {
        // Immagine nel post
        Image(
            bitmap = viewPicture(picture).asImageBitmap(),
            contentDescription = "Post image",
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .clip(RoundedCornerShape(8.dp))
                .border( 1.dp, Color.Gray,RoundedCornerShape(8.dp))
                .clickable { showImageDialog = true },
            contentScale = ContentScale.Crop
        )

        // Dialog fullscreen
        if (showImageDialog) {
            FullscreenImageDialog(
                imageBitmap = viewPicture(picture).asImageBitmap(),
                onDismiss = { showImageDialog = false }
            )
        }
    }
}
