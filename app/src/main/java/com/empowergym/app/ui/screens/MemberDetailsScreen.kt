package com.empowergym.app.ui.screens

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.empowergym.app.data.MemberRepository
import com.empowergym.app.ui.theme.PrimaryBlue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemberDetailsScreen(
    memberId: String,
    onBack: () -> Unit,
    onUpdateFees: () -> Unit,
    onDeleted: () -> Unit = onBack
) {
    val member = MemberRepository.members.find { it.id == memberId } ?: return
    var showDeleteConfirm by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Member Details") },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.Filled.ArrowBack, contentDescription = "Back") }
                },
                actions = {
                    IconButton(onClick = { showDeleteConfirm = true }) {
                        Icon(Icons.Filled.Delete, contentDescription = "Delete member", tint = Color(0xFFD32F2F))
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding).padding(16.dp).fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val bitmap = remember(member.photoPath) {
                member.photoPath?.let { path -> BitmapFactory.decodeFile(path)?.asImageBitmap() }
            }
            Box(
                modifier = Modifier.size(90.dp).clip(CircleShape).background(PrimaryBlue.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                if (bitmap != null) {
                    Image(bitmap = bitmap, contentDescription = "Member photo", modifier = Modifier.size(90.dp).clip(CircleShape))
                } else {
                    Icon(Icons.Filled.Person, contentDescription = null, modifier = Modifier.size(56.dp), tint = PrimaryBlue)
                }
            }
            Spacer(Modifier.height(8.dp))
            Text(member.name, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Text(member.id, color = Color.Gray)

            Spacer(Modifier.height(16.dp))
            Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)) {
                Column(Modifier.padding(16.dp)) {
                    DetailRow("Phone", member.whatsapp)
                    DetailRow("Joining", member.joiningDate)
                    DetailRow("Type", member.type.name.lowercase().replaceFirstChar { it.uppercase() })
                    DetailRow("Package", member.pkg.label)
                }
            }

            Spacer(Modifier.height(16.dp))
            Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)) {
                Column(Modifier.padding(16.dp)) {
                    Text("Payment Summary", fontWeight = FontWeight.Medium)
                    Spacer(Modifier.height(8.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Paid Months")
                        Text("${member.paidMonths} / ${member.totalMonths}", fontWeight = FontWeight.Bold)
                    }
                    Spacer(Modifier.height(8.dp))
                    LinearProgressIndicator(
                        progress = { member.paidMonths / member.totalMonths.toFloat() },
                        modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp))
                    )
                }
            }

            Spacer(Modifier.height(24.dp))
            Button(
                onClick = onUpdateFees,
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("UPDATE FEES")
            }
        }
    }

    if (showDeleteConfirm) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            title = { Text("Delete member?") },
            text = { Text("This will permanently remove ${member.name} (${member.id}). This can't be undone.") },
            confirmButton = {
                TextButton(onClick = {
                    MemberRepository.deleteMember(member.id)
                    showDeleteConfirm = false
                    onDeleted()
                }) { Text("Delete", color = Color(0xFFD32F2F)) }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirm = false }) { Text("Cancel") }
            }
        )
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, color = Color.Gray)
        Text(value, fontWeight = FontWeight.Medium)
    }
}
