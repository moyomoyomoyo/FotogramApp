package com.example.myfotogramapp.view.dialogs

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.myfotogramapp.view.components.LocationPicker
import com.mapbox.geojson.Point


@Composable
fun LocationPickerDialog(
    startLocation: Point = Point.fromLngLat(9.2319522, 45.4759249),
    onDismiss: () -> Unit,
    onLocationSelected: (Point) -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            tonalElevation = 6.dp,
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .fillMaxHeight(0.9f)
        ) {
            LocationPicker(
                startLocation = startLocation,
                onLocationSelected = { point ->
                    onLocationSelected(point)
                    onDismiss()
                }
            )
        }
    }
}
