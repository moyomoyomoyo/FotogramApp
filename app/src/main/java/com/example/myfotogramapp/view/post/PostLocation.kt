package com.example.myfotogramapp.view.post

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myfotogramapp.application.location.LocationManager
import com.example.myfotogramapp.post.model.PostEntity
import com.example.myfotogramapp.view.dialogs.LocationViewerDialog
import com.example.myfotogramapp.view.location.RequestLocationPermission
import com.mapbox.geojson.Point


@SuppressLint("MissingPermission")
@Composable
fun PostLocation(post: PostEntity){

    if(post.location == null){
        return
    }

    val context = LocalContext.current
    val locationManager = remember { LocationManager(context) }
    var userLat by remember { mutableStateOf<Double?>(null) }
    var userLon by remember { mutableStateOf<Double?>(null) }
    var isNear by remember { mutableStateOf<Boolean?>(null) }
    var distance by remember { mutableStateOf<Double?>(null) }
    var requestPermission by remember { mutableStateOf(false) }  // ← AGGIUNGI
    var permissionGranted by remember { mutableStateOf(false) }   // ← AGGIUNGI


    var showMapDialog by remember { mutableStateOf(false) }


    Row(
        modifier = Modifier
            .clickable {
                showMapDialog = true
                requestPermission = true
            }
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = Icons.Default.LocationOn,
            contentDescription = "location icon",
            tint = Color(0xFF7d0885),
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(2.dp))
        Column {
            Text(
                text = "Open Map",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = Color(0xFF7d0885)
            )
        }
    }

    // ← Richiedi permesso solo quando necessario
    if (requestPermission) {
        RequestLocationPermission(
            onGranted = {
                Log.i("PostLocation", "Permission granted")
                permissionGranted = true
                requestPermission = false  // ← Reset
            },
            onDenied = {
                Log.i("PostLocation", "Permission denied")
                permissionGranted = false
                requestPermission = false
            },
            onDeniedForever = {
                Log.i("PostLocation", "Permission denied forever")
                permissionGranted = false
                requestPermission = false
                // Opzionale: mostra dialog per aprire settings
            }
        )
    }

    LaunchedEffect(permissionGranted){
        if (!permissionGranted) return@LaunchedEffect
        locationManager.getCurrentLocation { la, lo ->
            userLat = la
            userLon = lo
            val d = locationManager.distanceInKm(
                la,
                lo,
                post.location.latitude,
                post.location.longitude
            )
            distance = d
            isNear = d <= 5.0

        }
    }

    if (showMapDialog) {
        LocationViewerDialog(
            postLocation = Point.fromLngLat(
                post.location.longitude,
                post.location.latitude
            ),
            userLocation = userLon?.let { userLat?.let { it1 -> Point.fromLngLat(it, it1) } },
            isNear = isNear,
            distanceKm = distance,
            onDismiss = { showMapDialog = false }
        )

    }
}