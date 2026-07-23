package com.empowergym.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.empowergym.app.data.MemberRepository
import com.empowergym.app.data.PackageType
import com.empowergym.app.ui.theme.PrimaryBlue
import com.empowergym.app.ui.theme.SecondaryGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeeUpdateScreen(memberId: String, onBack: () -> Unit, onUpdated: () -> Unit) {
    val memberIndex = MemberRepository.members.indexOfFirst { it.id == memberId }
    if (memberIndex == -1) return
    val member = MemberRepository.members[memberIndex]

    // Periods depend on package type, Half Yearly included
    val periods: List<String> = when (member.pkg) {
        PackageType.MONTHLY -> listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")
        PackageType.QUARTERLY -> listOf("Q1", "Q2", "Q3", "Q4")
        PackageType.HALF_YEARLY -> listOf("H1", "H2")
        PackageType.YEARLY -> listOf("2026")
    }
    val paidCount = when (member.pkg) {
        PackageType.MONTHLY -> member.paidMonths
        PackageType.QUARTERLY -> member.paidMonths / 3
        PackageType.HALF_YEARLY -> member.paidMonths / 6
        PackageType.YEARLY -> if (member.paidMonths >= 12) 1 else 0
    }

    val selected = remember { mutableStateListOf<Int>() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Fee Update") },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.Filled.ArrowBack, contentDescription = "Back") }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp)) {
            Text(member.name, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
            Text("${member.id} \u2022 ${member.pkg.label}", color = Color.Gray)
            Spacer(Modifier.height(16.dp))
            Text("Payment Periods", fontWeight = FontWeight.Medium)
            Spacer(Modifier.height(8.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.weight(1f, fill = false)
            ) {
                items(periods.size) { i ->
                    val isPaid = i < paidCount
                    val isSelected = selected.contains(i)
                    val bg = when {
                        isPaid -> SecondaryGreen.copy(alpha = 0.15f)
                        isSelected -> PrimaryBlue.copy(alpha = 0.15f)
                        else -> Color(0xFFECEFF1)
                    }
                    val borderColor = when {
                        isPaid -> SecondaryGreen
                        isSelected -> PrimaryBlue
                        else -> Color.Transparent
                    }
                    Column(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(bg)
                            .clickable(enabled = !isPaid) {
                                if (isSelected) selected.remove(i) else selected.add(i)
                            }
                            .padding(12.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(periods[i], fontWeight = FontWeight.Bold, color = if (isPaid) SecondaryGreen else if (isSelected) PrimaryBlue else Color.DarkGray)
                        if (isPaid) Text("Paid", style = MaterialTheme.typography.labelSmall, color = SecondaryGreen)
                    }
                }
            }

            Spacer(Modifier.height(12.dp))
            Row {
                LegendDot(SecondaryGreen, "Paid")
                Spacer(Modifier.width(16.dp))
                LegendDot(PrimaryBlue, "Selected")
                Spacer(Modifier.width(16.dp))
                LegendDot(Color(0xFFECEFF1), "Pending")
            }

            Spacer(Modifier.height(20.dp))
            Button(
                onClick = {
                    if (selected.isNotEmpty()) {
                        val unitMonths = when (member.pkg) {
                            PackageType.MONTHLY -> 1
                            PackageType.QUARTERLY -> 3
                            PackageType.HALF_YEARLY -> 6
                            PackageType.YEARLY -> 12
                        }
                        val newPaidMonths = (member.paidMonths + selected.size * unitMonths)
                            .coerceAtMost(member.totalMonths)
                        MemberRepository.members[memberIndex] = member.copy(paidMonths = newPaidMonths)
                        onUpdated()
                    }
                },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = SecondaryGreen)
            ) {
                Text("UPDATE PAYMENT")
            }
        }
    }
}

@Composable
private fun LegendDot(color: Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.size(10.dp).clip(RoundedCornerShape(5.dp)).background(color))
        Spacer(Modifier.width(4.dp))
        Text(label, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
    }
}
