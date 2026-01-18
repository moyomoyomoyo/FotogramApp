package com.example.myfotogramapp.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.GridOn
import androidx.compose.material.icons.outlined.List
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

enum class ViewMode {
    LIST, GRID
}

@Composable
fun ViewModeToggle(
    currentMode: ViewMode,
    onModeChange: (ViewMode) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = "Posts",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )

        Row(
            modifier = Modifier
                .background(
                    color = Color(0xFFE0E0E0),
                    shape = RoundedCornerShape(8.dp)
                )
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        Color(0xFF7d0885).copy(alpha = if (currentMode == ViewMode.GRID) 1f else 0f),
                        shape = RoundedCornerShape(topStart = 8.dp, bottomStart = 8.dp)
                    )
                    .clickable { onModeChange(ViewMode.GRID) }, contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.GridOn,
                    contentDescription = "Vista griglia",
                    tint = if (currentMode == ViewMode.GRID) Color.White else Color.Gray

                )
            }

            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        Color(0xFF7d0885).copy(alpha = if (currentMode == ViewMode.LIST) 1f else 0f),
                        shape = RoundedCornerShape(topEnd = 8.dp, bottomEnd = 8.dp)
                    )
                    .clickable { onModeChange(ViewMode.LIST) }, contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.List,
                    contentDescription = "Vista lista",
                    tint = if (currentMode == ViewMode.LIST) Color.White else Color.Gray
                )
            }
        }
    }
}