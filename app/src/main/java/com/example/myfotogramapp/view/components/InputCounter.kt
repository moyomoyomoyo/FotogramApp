package com.example.myfotogramapp.view.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun InputCounter(
    currentLength: Int,
    maxLength: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp),
        horizontalAlignment = androidx.compose.ui.Alignment.End
    ) {
        Text(
            text = "$currentLength/$maxLength",
            style = MaterialTheme.typography.bodySmall,
            color = if (currentLength > maxLength) Color.Red else Color.Gray,
            modifier = modifier
        )
    }

}