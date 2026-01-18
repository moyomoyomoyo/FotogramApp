package com.example.myfotogramapp.view.dialogs


import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.myfotogramapp.view.location.LocationViewer
import com.mapbox.geojson.Point

@Composable
fun LocationViewerDialog(
    postLocation: Point,
    userLocation: Point?=null,
    distanceKm: Double?,
    isNear: Boolean?,
    onDismiss: () -> Unit
) {

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
            ) {

                // MAPPA
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(630.dp)
                ) {
                    LocationViewer(
                        postLocation = postLocation,
                        userLocation = userLocation,
                        isNear = isNear?:false
                    )
                }

                // sezione sotto la mappa
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .background(
                            color = when {
                                isNear == null || distanceKm == null -> Color(0xFF7d0085).copy(alpha = 0.1f)
                                !isNear -> Color(0xFFFFEBEE)
                                else -> Color(0xFFE8F5E9)
                            },
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (distanceKm == null || isNear == null || userLocation == null) {
                        Log.i("LocationViewerDialog", "User null: $distanceKm km e isNear=$isNear")

//                        when{
//                            userLocation == null && distanceKm == null && isNear == null -> {
                                Text(
                                    text = "📍 Post location",
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.Medium,
                                    color = Color(0xFF7d0885)
                                )
//                            }
//                            else -> {
//                                Text(
//                                    text = "📍 Post location",
//                                    style = MaterialTheme.typography.bodyLarge,
//                                    fontWeight = FontWeight.Medium,
//                                    color = Color(0xFF7d0885)
//                                )
//                            }
//                        }



                    } else if (!isNear) {
                        if (userLocation.latitude() != 0.0 && postLocation.latitude() != 0.0) {
                            Log.i(
                                "LocationViewerDialog",
                                "User is too far: $distanceKm km e isNear=$isNear user location=${userLocation.latitude()} and post location=${postLocation.latitude()}"
                            )
                        }
                        Text(
                            text = "⚠️ You're too far away",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFD32F2F)
                        )
                        Text(
                            text = "Must be within 5 km",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF757575)
                        )
                    } else {
                        Log.i(
                            "LocationViewerDialog",
                            "User is too far: $distanceKm km e isNear=$isNear"
                        )

                        Text(
                            text = "✨ You're nearby!",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2E7D32)
                        )
                        Text(
                            text = "About ${"%.1f".format(distanceKm)} km away",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF616161)
                        )
                    }
                }

                // BOTTONE CHIUDI
                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .height(50.dp),
                    shape = RoundedCornerShape(15.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF7d0885)
                    )
                ) {
                    Text(
                        text = "Close",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))


//                // SEZIONE INFORMATIVA SOTTO LA MAPPA
//                Column(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(16.dp)
//                ) {
//
//                    if (distanceKm == null) {
//                        Text(
//                            text = "Calculating distance...",
//                            style = MaterialTheme.typography.bodyMedium
//                        )
//                    } else if (!isNear) {
//                        Text(
//                            text = "You're too far",
//                            style = MaterialTheme.typography.bodyMedium,
//                            color = Color.Red
//                        )
//                    } else {
//
//                        Text(
//                            text = "You're about ${"%.1f".format(distanceKm)} km away",
//                            style = MaterialTheme.typography.bodyMedium,
//                            color = Color(0xFF1E88E5)
//                        )
//                    }
//                }

//                // BOTTONE CHIUDI
//                Button(
//                    onClick = onDismiss,
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(16.dp)
//                ) {
//                    Text("Close")
//                }
            }
        }
//         Legenda
        Column(
            modifier = Modifier
                .padding(16.dp)
                .background(
                    color = Color.White.copy(alpha = 0.9f),
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(12.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .background(Color(0xFF7d0885), CircleShape)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Post location")
            }

            if (isNear == true) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .background(Color(0xFFFB2B3A), CircleShape)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Your location")
                }
            }
        }

    }
}