package com.example.myfotogramapp.view.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ProfileStats(
    posts: Int,
    followers: Int,
    following: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        StatBox(number = posts, label = "Post")
        StatBox(number = followers, label = "Follower")
        StatBox(number = following, label = "Following")
    }
}

@Composable
private fun StatBox(number: Int, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {

        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.padding(5.dp))
        Text(
            text = number.toString(),
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.padding(10.dp))
    }
}
