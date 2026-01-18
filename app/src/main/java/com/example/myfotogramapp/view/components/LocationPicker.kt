package com.example.myfotogramapp.view.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.unit.dp
import com.mapbox.geojson.Point
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState

@SuppressLint("MissingPermission")
@Composable
fun LocationPicker(
    modifier: Modifier = Modifier,
    startLocation: Point = Point.fromLngLat(9.2319522, 45.4759249),
    onLocationSelected: (Point) -> Unit
) {
    var selectedLocation by remember { mutableStateOf<Point?>(null) }

    val mapViewportState = rememberMapViewportState {
        setCameraOptions {
            zoom(15.0)
            center(startLocation)
            pitch(0.0)
            bearing(0.0)
        }
    }

    Column(modifier = modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            MapboxMap(
                modifier = Modifier.fillMaxSize(),
                mapViewportState = mapViewportState
            )

            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = "Center marker",
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(48.dp)
                    .offset(y = (-24).dp),
                tint = Color(0xFF7d0885)
            )

            Button(
                onClick = {
                    val center = mapViewportState.cameraState?.center
                    if (center != null) {
                        selectedLocation = center
                        onLocationSelected(center)
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF7d0885)
                ),
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Save location"
                )
            }
        }

        selectedLocation?.let { loc ->
            Text(
                text = "Lat: ${loc.latitude()}, Lng: ${loc.longitude()}",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}
