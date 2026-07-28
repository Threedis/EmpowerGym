package com.empowergym.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.empowergym.app.data.Member
import com.empowergym.app.data.MemberRepository
import com.empowergym.app.ui.theme.PrimaryBlue
import com.empowergym.app.ui.theme.SecondaryGreen

/**
 * Fees tab: focused on payment status, not the general member directory.
 * Tapping a member here jumps straight into the fee-update flow for them.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeesScreen(onCollectFee: (String) -> Unit) {
    val members = MemberRepository.members
    val due = members.filter { it.paidMonths < it.totalMonths }
    val paidUp = members.filter { it.paidMonths >= it.totalMonths }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Fees") }) }
    ) { padding ->
        if (members.isEmpty()) {
            Box(modifier = Modifier.padding(padding).fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Filled.AttachMoney, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(48.dp))
                    Spacer(Modifier.height(8.dp))
                    Text("No members yet", color = Color.Gray)
                }
            }
            return@Scaffold
        }

        LazyColumn(
            modifier = Modifier.padding(padding).padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item { Spacer(Modifier.height(4.dp)) }
            if (due.isNotEmpty()) {
                item { Text("Payment Due (${due.size})", fontWeight = FontWeight.Bold, color = Color(0xFFD32F2F)) }
                items(due, key = { it.id }) { m -> FeeRow(m, onCollectFee) }
            }
            if (paidUp.isNotEmpty()) {
                item { Spacer(Modifier.height(8.dp)); Text("Fully Paid (${paidUp.size})", fontWeight = FontWeight.Bold, color = SecondaryGreen) }
                items(paidUp, key = { it.id }) { m -> FeeRow(m, onCollectFee) }
            }
            item { Spacer(Modifier.height(24.dp)) }
        }
    }
}

@Composable
private fun FeeRow(member: Member, onClick: (String) -> Unit) {
    val isDue = member.paidMonths < member.totalMonths
    Card(
        modifier = Modifier.clickable { onClick(member.id) },
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(40.dp).clip(CircleShape)
                    .background((if (isDue) Color(0xFFD32F2F) else SecondaryGreen).copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Filled.Person, contentDescription = null, tint = if (isDue) Color(0xFFD32F2F) else SecondaryGreen)
            }
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(member.name, fontWeight = FontWeight.Medium)
                Text("${member.id} • ${member.pkg.label}", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            }
            Text(
                "${member.paidMonths}/${member.totalMonths}",
                fontWeight = FontWeight.Bold,
                color = if (isDue) Color(0xFFD32F2F) else SecondaryGreen
            )
            Spacer(Modifier.width(6.dp))
            Icon(Icons.Filled.ChevronRight, contentDescription = null, tint = Color.Gray)
        }
    }
}
