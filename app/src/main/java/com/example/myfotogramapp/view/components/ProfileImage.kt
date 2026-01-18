package com.example.myfotogramapp.view.components


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.myfotogramapp.R

@Composable
fun ProfileImage(
    picture: String,
    size: Dp = 120.dp,
    isFriend: Boolean = false
) {
    val maxSize = 80_000

    val bitmap = try {
        if(picture.isEmpty()) null
        else viewPicture(picture).asImageBitmap()
    } catch (e: Exception) {
        null
    }

    // controllo la validità dell'immagine

    // se non valida -> mostro il placeholder
    if (picture.isEmpty() || bitmap == null || picture.length > maxSize) {
        Box(
            modifier = Modifier
                .size(size)
                .then(
                    if (isFriend) {
                        Modifier.border(
                            width = 1.8.dp,
                            color = Color(0xFFFFC107),
                            shape = CircleShape
                        )
                    } else Modifier.border(
                        width = 1.dp,
                        color = Color.Black,
                        shape = CircleShape
                    )
                )
                .clip(CircleShape)
                .background(Color(0xFFE0E0E0)),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(R.drawable.no_photo) ,
                contentDescription = "Placeholder",
                modifier = Modifier.size(100.dp),
                contentScale = ContentScale.Crop
            )
        }
    } else {
        // immagine valida → la mostro
        Image(
            bitmap = viewPicture(picture).asImageBitmap(),
            contentDescription = "Profile Image",
            modifier = Modifier
                .size(size)
                .then(
                    if (isFriend) {
                        Modifier.border(
                            width = 3.dp, color = Color(0xFF7d0885), shape = CircleShape
                        )
                    } else Modifier.border(
                        width = 0.5.dp,
                        color = Color.Black,
                        shape = CircleShape
                    )
                )
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )

    }

}