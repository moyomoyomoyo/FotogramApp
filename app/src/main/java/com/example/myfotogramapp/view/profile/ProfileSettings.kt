package com.example.myfotogramapp.view.profile


import android.annotation.SuppressLint
import android.icu.util.Calendar
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myfotogramapp.application.utilis.isValidUsername
import com.example.myfotogramapp.application.utilis.normalizeText
import com.example.myfotogramapp.navigation.NavigationViewModel
import com.example.myfotogramapp.navigation.Screen
import com.example.myfotogramapp.user.model.UserEntity
import com.example.myfotogramapp.user.model.UserInfoUpdateDto
import com.example.myfotogramapp.user.viewmodel.UserViewModel
import com.example.myfotogramapp.view.components.Header
import com.example.myfotogramapp.view.components.ImagePicker
import com.example.myfotogramapp.view.components.InputCounter
import com.example.myfotogramapp.view.components.LoadingScreen
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId

@SuppressLint("DefaultLocale")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileSettings(
    user: UserEntity?,
    nav: NavigationViewModel,
    userViewModel: UserViewModel,
    modifier: Modifier
) {
    if (user == null) {
        LoadingScreen()
        return
    }

    var username by remember { mutableStateOf(user.username) }
    var bio by remember { mutableStateOf(user.bio) }
    var selectedDate by remember { mutableStateOf(user.dateOfBirth) }
    var profilePicBase64 by remember { mutableStateOf(user.profilePicture) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showErrorUsername by remember { mutableStateOf(false) }
    var showErrorBio by remember { mutableStateOf(false) }
    val usernameMaxLength = 20
    val bioMaxLength = 100

    // Data massima: 16 anni fa
    val maxDate = Calendar.getInstance().apply {
        add(Calendar.YEAR, -16)
    }.timeInMillis

    // data iniziale = data corrente altrimenti a maxDate
    val initialMillis = if (selectedDate.isNotBlank()) {
        LocalDate.parse(selectedDate).atStartOfDay(ZoneId.systemDefault()).toInstant()
            .toEpochMilli()
    } else {
        maxDate
    }

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = initialMillis,
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                return utcTimeMillis <= maxDate
            }
        }
    )

    val scrollState = rememberScrollState()

    // Verifica se ci sono modifiche
    val hasChanges = username != user.username ||
            bio != user.bio ||
            selectedDate != user.dateOfBirth ||
            profilePicBase64 != user.profilePicture

    Column(modifier.fillMaxSize()) {

        Header(nav, user)

        Column(
            modifier
                .fillMaxSize()
                .background(Color(0xFFe5d3e5))
                .verticalScroll(scrollState)
                .padding(bottom = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(24.dp))

            // Card Immagine Profilo
            Card(
                modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {
                Column(
                    modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "Profile Picture",
                        style = MaterialTheme.typography.titleMedium,
                        fontSize = 20.sp,
                        color = Color(0xFF7d0885),
                        fontWeight = FontWeight.Bold,
                    )

                    Spacer(Modifier.height(16.dp))

                    ImagePicker(
                        initialImage = profilePicBase64,
                        onImageSelected = { img ->
                            if (img != null) {
                                profilePicBase64 = img
                            }
                        }
                    )

                    Spacer(modifier.height(8.dp))

                    Text(
                        "Tap to change",
                        color = Color.Gray
                    )

                }

                Column(
                    modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "Account Information",
                        style = MaterialTheme.typography.titleMedium,
                        fontSize = 20.sp,
                        color = Color(0xFF7d0885),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    // Username
                    OutlinedTextField(
                        value = username,
                        onValueChange = { username = it },
                        label = { Text("Username") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Filled.Person,
                                contentDescription = null,
                                tint = Color(0xFF7d0885)
                            )
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF7d0885),
                            focusedLabelColor = Color(0xFF7d0885),
                            cursorColor = Color(0xFF7d0885)
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        textStyle = TextStyle(fontSize = 16.sp),
                        singleLine = true
                    )

                    InputCounter(username.length, 20, modifier = Modifier)

                    if (showErrorUsername) {

                        val errorMessage = when {
                            username.length > usernameMaxLength -> "Username must be less than $usernameMaxLength characters"

                            username.isBlank() ->
                                "Username cannot be empty"

                            !isValidUsername(username) -> "Username can contain only letters, numbers and _"

                            else -> ""
                        }

                        Text(
                            text = errorMessage,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }

                    Spacer(Modifier.height(16.dp))

                    // Data di nascita
                    OutlinedTextField(
                        value = selectedDate,
                        onValueChange = {},
                        label = { Text("Date of Birth") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = null,
                                tint = Color(0xFF7d0885)
                            )
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF7d0885),
                            focusedLabelColor = Color(0xFF7d0885),
                            cursorColor = Color(0xFF7d0885)
                        ),
                        trailingIcon = {
                            IconButton(onClick = { showDatePicker = true }) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Select date",
                                    tint = Color(0xFF7d0885)
                                )
                            }
                        },
                        textStyle = TextStyle(fontSize = 16.sp),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        readOnly = true
                    )

                    Spacer(Modifier.height(16.dp))

                    Column(
                        modifier
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "Biography",
                            style = MaterialTheme.typography.titleMedium,

                            fontSize = 20.sp,
                            color = Color(0xFF7d0885),
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        OutlinedTextField(
                            value = bio,
                            onValueChange = { bio = it },
                            placeholder = { Text("Add your bio...") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp),
                            maxLines = 5,
                            textStyle = TextStyle(fontSize = 16.sp),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFF7d0885),
                                focusedLabelColor = Color(0xFF7d0885),
                                cursorColor = Color(0xFF7d0885)
                            )
                        )

                        InputCounter(
                            bio.length,
                            bioMaxLength,
                            modifier = Modifier.align(Alignment.End)
                        )

                        if (showErrorBio) {
                            Spacer(Modifier.height(8.dp))

                            val errorMessage = when {
                                bio.length > bioMaxLength -> "Bio must be less than $bioMaxLength characters"
                                else -> ""
                            }

                            Text(
                                text = errorMessage,
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodyMedium,

                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                        }
                    }
                }

                val scope = rememberCoroutineScope()

                // salvataggio modifiche
                Button(
                    onClick = {
                        if (hasChanges) {
                            showErrorUsername = username.isBlank() ||
                                    !isValidUsername(username) ||
                                    username.length > usernameMaxLength
                            showErrorBio = bio.length > bioMaxLength

                            if (!showErrorUsername && !showErrorBio) {
//
//                                val userUpdated = UserInfoUpdateDto(
//                                    username = username,
//                                    bio = normalizeText(bio, true),
//                                    dateOfBirth = selectedDate
//                                )
                                scope.launch {
                                    try{
                                        userViewModel.updateUserInfo(
                                            username = username,
                                            bio = normalizeText(bio, true),
                                            dateOfBirth = selectedDate,
                                            picture = if (profilePicBase64 == user.profilePicture) "" else profilePicBase64
                                        )
                                    } finally {
                                        nav.navigateTo(Screen.PROFILE)
                                    }

                                }
                            }
                        }
                    },
                    modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .padding(horizontal = 16.dp),
                    enabled = hasChanges, // && !isSaving
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF7d0885),
                        disabledContainerColor = Color.Gray
                    )
                ) {
                    Icon(
                        imageVector = Icons.Filled.Save,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        "Save Changes",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(Modifier.height(12.dp))

                // Pulsante Cancel
                OutlinedButton(
                    onClick = { nav.navigateBack() },
                    modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Cancel", fontSize = 16.sp)
                }

                Spacer(Modifier.height(24.dp))
            }

            Spacer(Modifier.height(40.dp))

        }

    }

    // DatePicker Dialog
    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val calendar = Calendar.getInstance().apply {
                            timeInMillis = millis
                        }
                        val formattedDate = String.format(
                            "%04d-%02d-%02d",
                            calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH) + 1,
                            calendar.get(Calendar.DAY_OF_MONTH)
                        )
                        selectedDate = formattedDate
                    }
                    showDatePicker = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Close")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}