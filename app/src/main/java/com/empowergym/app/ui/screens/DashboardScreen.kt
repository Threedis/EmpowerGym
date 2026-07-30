package com.empowergym.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.empowergym.app.data.MemberRepository
import com.empowergym.app.ui.theme.PrimaryBlue
import com.empowergym.app.ui.theme.SecondaryGreen

@Composable
fun DashboardScreen(onRegisterMember: () -> Unit, onCollectFees: () -> Unit) {
    val members = MemberRepository.members
    val feesDue = members.count { it.paidMonths < it.totalMonths }
    val fullyPaid = members.count { it.paidMonths >= it.totalMonths }
    // "Expiring soon" = paid but within 1 unit of running out (a simple, real heuristic instead of a fake number)
    val expiringSoon = members.count { it.paidMonths in 1 until it.totalMonths && it.totalMonths - it.paidMonths <= 1 }

    Scaffold(topBar = { EmpowerTopBar("Dashboard") }) { padding ->
    LazyColumn(
        modifier = Modifier
            .padding(padding)
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item { Spacer(Modifier.height(12.dp)) }
        item {
            Text("Welcome Back \uD83D\uDC4B", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Text("Have a great workout ahead", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
        }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                StatCard(Modifier.weight(1f), Icons.Filled.Group, "Members", members.size.toString(), PrimaryBlue)
                StatCard(Modifier.weight(1f), Icons.Filled.AttachMoney, "Fees Due", feesDue.toString(), Color(0xFFD32F2F))
            }
        }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                StatCard(Modifier.weight(1f), Icons.Filled.CheckCircle, "Fully Paid", fullyPaid.toString(), SecondaryGreen)
                StatCard(Modifier.weight(1f), Icons.Filled.CalendarMonth, "Expiring Soon", expiringSoon.toString(), Color(0xFFF9A825))
            }
        }
        item {
            Button(
                onClick = onRegisterMember,
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Filled.PersonAdd, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Register New Member")
            }
        }
        item {
            Button(
                onClick = onCollectFees,
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = SecondaryGreen)
            ) {
                Icon(Icons.Filled.CreditCard, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Update Fees")
            }
        }
        item {
            Text("Recent Members", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }
        if (members.isEmpty()) {
            item {
                Card(shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Info, contentDescription = null, tint = Color.Gray)
                        Spacer(Modifier.width(12.dp))
                        Text("No members registered yet", color = Color.Gray)
                    }
                }
            }
        } else {
            items(members.takeLast(5).reversed(), key = { it.id }) { member ->
                Card(shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Filled.Person, contentDescription = null, tint = PrimaryBlue)
                        Spacer(Modifier.width(12.dp))
                        Column {
                            Text(member.name, fontWeight = FontWeight.Medium)
                            Text("${member.joiningDate} \u2022 ${member.pkg.label}", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                        }
                    }
                }
            }
        }
        item { Spacer(Modifier.height(24.dp)) }
    }
    }
}

@Composable
private fun StatCard(modifier: Modifier, icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, value: String, color: Color) {
    Card(modifier = modifier, shape = RoundedCornerShape(16.dp)) {
        Column(Modifier.padding(16.dp)) {
            Icon(icon, contentDescription = null, tint = color)
            Spacer(Modifier.height(8.dp))
            Text(value, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Text(label, style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
        }
    }
}
