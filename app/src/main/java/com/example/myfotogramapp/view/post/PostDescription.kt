package com.example.myfotogramapp.post.view

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myfotogramapp.post.model.PostEntity
import com.example.myfotogramapp.view.post.PostLocation
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@Composable
fun PostDescription(
    text: String,
    post: PostEntity,
    collapsedMaxLines: Int = 3
) {
    var expanded by remember { mutableStateOf(false) }

    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        fontSize = 16.sp,
        maxLines = if (expanded) Int.MAX_VALUE else collapsedMaxLines,
        overflow = TextOverflow.Ellipsis,
        modifier = Modifier.clickable { expanded = !expanded }
    )

    Spacer(modifier = Modifier.height(8.dp))

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = formatDateTimeLocal(post.createdAt),
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray
        )
        if (post.location != null) {
            PostLocation(post)
        }
    }
}


fun formatDateTimeLocal(isoDateTime: String): String {
    return try {
        val zonedDateTime = ZonedDateTime.parse(isoDateTime)
        val localDateTime = zonedDateTime.toLocalDateTime()
        val formatter = DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy")
        localDateTime.format(formatter)
    } catch (e: Exception) {
        Log.e("DateFormat", "Errore formattazione data: ${e.message}")
        isoDateTime
    }
}