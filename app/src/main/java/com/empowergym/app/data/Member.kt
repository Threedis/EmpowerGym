package com.empowergym.app.data

import androidx.compose.runtime.mutableStateListOf

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
    val photoUrl: String? = null
)

object MemberRepository {
    val members = mutableStateListOf(
        Member(
            id = "ES2026072001",
            name = "Rahul Sharma",
            whatsapp = "9876543210",
            alternate = "9876501234",
            joiningDate = "21 Jul 2026",
            type = MembershipType.CARDIO,
            pkg = PackageType.MONTHLY,
            paidMonths = 6
        ),
        Member(
            id = "ES2026072002",
            name = "Amit Kumar",
            whatsapp = "9876543211",
            joiningDate = "18 Jul 2026",
            type = MembershipType.STRENGTH,
            pkg = PackageType.QUARTERLY,
            paidMonths = 3
        ),
        Member(
            id = "ES2026072003",
            name = "Rohit Singh",
            whatsapp = "9876543212",
            joiningDate = "10 Jul 2026",
            type = MembershipType.CARDIO,
            pkg = PackageType.YEARLY,
            paidMonths = 12
        ),
        Member(
            id = "ES2026072004",
            name = "Neha Verma",
            whatsapp = "9876543213",
            joiningDate = "05 Jul 2026",
            type = MembershipType.CARDIO,
            pkg = PackageType.HALF_YEARLY,
            paidMonths = 4
        ),
        Member(
            id = "ES2026072005",
            name = "Pooja Patel",
            whatsapp = "9876543214",
            joiningDate = "01 Jul 2026",
            type = MembershipType.STRENGTH,
            pkg = PackageType.QUARTERLY,
            paidMonths = 1
        )
    )

    fun nextMemberId(): String {
        val today = "20260723" // yyyymmdd placeholder, formatted at call site normally
        val count = members.size + 1
        return "ES$today${count.toString().padStart(2, '0')}"
    }
}
