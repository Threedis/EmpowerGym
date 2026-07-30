package com.empowergym.app.ui.screens

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.empowergym.app.data.Member
import com.empowergym.app.data.MemberRepository
import com.empowergym.app.data.MembershipType
import com.empowergym.app.data.PackageType
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterMemberScreen(onBack: () -> Unit, onRegistered: () -> Unit) {
    var name by remember { mutableStateOf("") }
    var whatsapp by remember { mutableStateOf("") }
    var alternate by remember { mutableStateOf("") }
    var type by remember { mutableStateOf(MembershipType.CARDIO) }
    var pkg by remember { mutableStateOf(PackageType.MONTHLY) }
    val memberId = remember { MemberRepository.nextMemberId() }
    var photoPath by remember { mutableStateOf<String?>(null) }

    var joiningDateMillis by remember { mutableStateOf(System.currentTimeMillis()) }
    var showDatePicker by remember { mutableStateOf(false) }
    val dateFormatter = remember { SimpleDateFormat("dd MMM yyyy", Locale.US) }

    val launchCamera = rememberCameraCapture { path -> photoPath = path }

    Scaffold(
        topBar = {
            EmpowerTopBar(
                pageTitle = "Register Member",
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            LabeledField("Member ID", memberId, readOnly = true, onValueChange = {})
            LabeledField("Name *", name, onValueChange = { name = it })
            LabeledField("WhatsApp *", whatsapp, onValueChange = { whatsapp = it })
            LabeledField("Alternate Number", alternate, onValueChange = { alternate = it })

            Text("Joining Date", fontWeight = FontWeight.Medium)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .clickable { showDatePicker = true }
                    .padding(vertical = 8.dp)
            ) {
                Icon(Icons.Filled.CalendarMonth, contentDescription = null)
                Spacer(Modifier.width(6.dp))
                Text(dateFormatter.format(Date(joiningDateMillis)))
            }

            Text("Membership Type *", fontWeight = FontWeight.Medium)
            Row {
                RadioOption("Cardio", type == MembershipType.CARDIO) { type = MembershipType.CARDIO }
                Spacer(Modifier.width(16.dp))
                RadioOption("Strength", type == MembershipType.STRENGTH) { type = MembershipType.STRENGTH }
            }

            // Package selector now includes Half Yearly
            Text("Package *", fontWeight = FontWeight.Medium)
            Column {
                PackageType.entries.forEach { option ->
                    RadioOption(option.label, pkg == option) { pkg = option }
                }
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val bitmap = remember(photoPath) {
                    photoPath?.let { path -> BitmapFactory.decodeFile(path)?.asImageBitmap() }
                }
                Box(
                    modifier = Modifier.size(90.dp).clip(CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    if (bitmap != null) {
                        Image(
                            bitmap = bitmap,
                            contentDescription = "Captured member photo",
                            modifier = Modifier.size(90.dp).clip(CircleShape)
                        )
                    } else {
                        Icon(Icons.Filled.Person, contentDescription = null, modifier = Modifier.size(60.dp), tint = Color.Gray)
                    }
                }
                Text(if (bitmap != null) "Photo captured" else "No Photo", color = Color.Gray)
                Spacer(Modifier.height(8.dp))
                OutlinedButton(onClick = { launchCamera() }) {
                    Icon(Icons.Filled.CameraAlt, contentDescription = null)
                    Spacer(Modifier.width(6.dp))
                    Text(if (bitmap != null) "Retake Photo" else "Capture Photo")
                }
            }

            Button(
                onClick = {
                    if (name.isNotBlank() && whatsapp.isNotBlank()) {
                        MemberRepository.addMember(
                            Member(
                                id = memberId,
                                name = name,
                                whatsapp = whatsapp,
                                alternate = alternate,
                                joiningDate = dateFormatter.format(Date(joiningDateMillis)),
                                type = type,
                                pkg = pkg,
                                photoPath = photoPath
                            )
                        )
                        onRegistered()
                    }
                },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("REGISTER MEMBER")
            }
            Spacer(Modifier.height(16.dp))
        }
    }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(initialSelectedDateMillis = joiningDateMillis)
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { joiningDateMillis = it }
                    showDatePicker = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Cancel") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

@Composable
private fun LabeledField(label: String, value: String, readOnly: Boolean = false, onValueChange: (String) -> Unit) {
    Column {
        Text(label, fontWeight = FontWeight.Medium)
        Spacer(Modifier.height(4.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            readOnly = readOnly,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp)
        )
    }
}

@Composable
private fun RadioOption(label: String, selected: Boolean, onClick: () -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        RadioButton(selected = selected, onClick = onClick)
        Text(label)
    }
}
