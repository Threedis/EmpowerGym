package com.empowergym.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.empowergym.app.data.MemberRepository
import com.empowergym.app.data.PackageType
import com.empowergym.app.ui.theme.PrimaryBlue
import com.empowergym.app.ui.theme.SecondaryGreen

@Composable
fun WhatsAppConfirmScreen(onDone: () -> Unit) {
    var notifyMember by remember { mutableStateOf(true) }
    var notifyOwner by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(32.dp))
        Icon(
            Icons.Filled.CheckCircle,
            contentDescription = null,
            tint = SecondaryGreen,
            modifier = Modifier.size(80.dp)
        )
        Spacer(Modifier.height(16.dp))
        Text("Payment Updated Successfully!", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(24.dp))
        Text("Send WhatsApp Notification?", fontWeight = FontWeight.Medium)

        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(checked = notifyMember, onCheckedChange = { notifyMember = it })
            Text("Member")
        }
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(checked = notifyOwner, onCheckedChange = { notifyOwner = it })
            Text("Gym Owner")
        }

        Spacer(Modifier.height(24.dp))
        Button(
            onClick = onDone,
            modifier = Modifier.fillMaxWidth().height(52.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(Icons.Filled.Send, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text("SEND")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportsScreen() {
    val members = MemberRepository.members
    val totalMembers = members.size
    val fullyPaid = members.count { it.paidMonths >= it.totalMonths }
    val due = totalMembers - fullyPaid
    val byPackage = PackageType.entries.associateWith { pkg -> members.count { it.pkg == pkg } }
    val byType = mapOf(
        "Cardio" to members.count { it.type.name == "CARDIO" },
        "Strength" to members.count { it.type.name == "STRENGTH" }
    )

    Scaffold(topBar = { TopAppBar(title = { Text("Reports") }) }) { padding ->
        if (members.isEmpty()) {
            Box(modifier = Modifier.padding(padding).fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Filled.BarChart, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(48.dp))
                    Spacer(Modifier.height(8.dp))
                    Text("No data yet — register members to see reports", color = Color.Gray)
                }
            }
            return@Scaffold
        }

        Column(
            modifier = Modifier.padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                ReportStat(Modifier.weight(1f), "Total Members", totalMembers.toString(), PrimaryBlue)
                ReportStat(Modifier.weight(1f), "Fully Paid", fullyPaid.toString(), SecondaryGreen)
            }
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                ReportStat(Modifier.weight(1f), "Payment Due", due.toString(), Color(0xFFD32F2F))
                ReportStat(Modifier.weight(1f), "Cardio / Strength", "${byType["Cardio"]} / ${byType["Strength"]}", PrimaryBlue)
            }

            Text("Members by Package", fontWeight = FontWeight.Bold)
            Card(shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp)) {
                    byPackage.forEach { (pkg, count) ->
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(pkg.label)
                            Text(count.toString(), fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ReportStat(modifier: Modifier, label: String, value: String, color: Color) {
    Card(modifier = modifier, shape = RoundedCornerShape(16.dp)) {
        Column(Modifier.padding(16.dp)) {
            Text(value, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = color)
            Text(label, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen() {
    var showClearConfirm by remember { mutableStateOf(false) }

    Scaffold(topBar = { TopAppBar(title = { Text("Settings") }) }) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp)) {
            Card(shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp)) {
                    Text("Empower Gym", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                    Text("Version 1.0", color = Color.Gray, style = MaterialTheme.typography.bodySmall)
                    Text(
                        "${MemberRepository.members.size} members stored on this device",
                        color = Color.Gray,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
            Spacer(Modifier.height(16.dp))
            OutlinedButton(
                onClick = { showClearConfirm = true },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFD32F2F))
            ) {
                Icon(Icons.Filled.DeleteForever, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Clear All Member Data")
            }
        }
    }

    if (showClearConfirm) {
        AlertDialog(
            onDismissRequest = { showClearConfirm = false },
            title = { Text("Clear all data?") },
            text = { Text("This will permanently delete every member and payment record on this device. This can't be undone.") },
            confirmButton = {
                TextButton(onClick = {
                    MemberRepository.members.toList().forEach { MemberRepository.deleteMember(it.id) }
                    showClearConfirm = false
                }) { Text("Clear Everything", color = Color(0xFFD32F2F)) }
            },
            dismissButton = {
                TextButton(onClick = { showClearConfirm = false }) { Text("Cancel") }
            }
        )
    }
}
