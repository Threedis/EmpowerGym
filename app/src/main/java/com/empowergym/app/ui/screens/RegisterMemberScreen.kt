package com.empowergym.app.ui.screens

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.empowergym.app.data.Member
import com.empowergym.app.data.MemberRepository
import com.empowergym.app.data.MembershipType
import com.empowergym.app.data.PackageType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterMemberScreen(onBack: () -> Unit, onRegistered: () -> Unit) {
    var name by remember { mutableStateOf("") }
    var whatsapp by remember { mutableStateOf("") }
    var alternate by remember { mutableStateOf("") }
    var type by remember { mutableStateOf(MembershipType.CARDIO) }
    var pkg by remember { mutableStateOf(PackageType.MONTHLY) }
    val memberId = remember { MemberRepository.nextMemberId() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Register Member") },
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
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.CalendarMonth, contentDescription = null)
                Spacer(Modifier.width(6.dp))
                Text("23-Jul-2026")
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
                Box(
                    modifier = Modifier
                        .size(90.dp)
                        .clip(CircleShape)
                        .then(Modifier),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Filled.Person, contentDescription = null, modifier = Modifier.size(60.dp), tint = Color.Gray)
                }
                Text("No Photo", color = Color.Gray)
                Spacer(Modifier.height(8.dp))
                OutlinedButton(onClick = { /* capture photo */ }) {
                    Icon(Icons.Filled.CameraAlt, contentDescription = null)
                    Spacer(Modifier.width(6.dp))
                    Text("Capture Photo")
                }
            }

            Button(
                onClick = {
                    if (name.isNotBlank() && whatsapp.isNotBlank()) {
                        MemberRepository.members.add(
                            Member(
                                id = memberId,
                                name = name,
                                whatsapp = whatsapp,
                                alternate = alternate,
                                joiningDate = "23 Jul 2026",
                                type = type,
                                pkg = pkg
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
