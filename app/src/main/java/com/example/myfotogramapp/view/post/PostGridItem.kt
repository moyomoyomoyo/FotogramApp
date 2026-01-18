package com.example.myfotogramapp.view.post


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.myfotogramapp.R
import com.example.myfotogramapp.navigation.NavigationViewModel
import com.example.myfotogramapp.post.model.PostEntity
import com.example.myfotogramapp.view.components.viewPicture

@Composable
fun PostGridItem(
    post: PostEntity,
    navViewModel: NavigationViewModel
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .aspectRatio(1f)
                .padding(2.dp)
                .clickable { navViewModel.navigateToPostDetail(post.id) }
        ) {

            // Provo a decodificare la bitmap
            val bitmap = try { if (post.contentPicture.isEmpty()) null else viewPicture(post.contentPicture) } catch (e: Exception) { null }

            if(post.contentPicture.isEmpty() || bitmap == null) {
                // Placeholder se non c'è immagine
                Box(
                    modifier = Modifier
                        .fillMaxSize()
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
                // Immagine del post
                viewPicture(post.contentPicture).let {
                    Image(
                        bitmap = it.asImageBitmap(),
                        contentDescription = "Post thumbnail",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
    }
}