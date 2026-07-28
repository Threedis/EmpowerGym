package com.empowergym.app.data

import androidx.compose.runtime.mutableStateListOf
import java.text.SimpleDateFormat
import java.util.*

enum class MembershipType { CARDIO, STRENGTH }

// Half Yearly added alongside Monthly / Quarterly / Yearly
enum class PackageType(val label: String) {
    MONTHLY("Monthly"),
    QUARTERLY("Quarterly"),
    HALF_YEARLY("Half Yearly"),
    YEARLY("Yearly")
}

data class Member(
    val id: String,
    val name: String,
    val whatsapp: String,
    val alternate: String = "",
    val joiningDate: String,
    val type: MembershipType,
    val pkg: PackageType,
    val paidMonths: Int = 0,
    val totalMonths: Int = 12,
    val photoPath: String? = null // local file path of captured photo, if any
)

object MemberRepository {
    // Starts empty — no dummy/sample data. Real members are added via Register Member.
    val members = mutableStateListOf<Member>()

    fun nextMemberId(): String {
        val today = SimpleDateFormat("yyyyMMdd", Locale.US).format(Date())
        val count = members.size + 1
        return "ES$today${count.toString().padStart(3, '0')}"
    }

    fun deleteMember(id: String) {
        members.removeAll { it.id == id }
    }

    fun updateMember(updated: Member) {
        val idx = members.indexOfFirst { it.id == updated.id }
        if (idx != -1) members[idx] = updated
    }
}
