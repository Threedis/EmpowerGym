package com.empowergym.app.data

import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import org.json.JSONArray
import org.json.JSONObject
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

private const val PREFS_NAME = "empower_gym_prefs"
private const val KEY_MEMBERS = "members_json"

private fun Member.toJson(): JSONObject = JSONObject().apply {
    put("id", id)
    put("name", name)
    put("whatsapp", whatsapp)
    put("alternate", alternate)
    put("joiningDate", joiningDate)
    put("type", type.name)
    put("pkg", pkg.name)
    put("paidMonths", paidMonths)
    put("totalMonths", totalMonths)
    put("photoPath", photoPath ?: "")
}

private fun memberFromJson(o: JSONObject): Member = Member(
    id = o.getString("id"),
    name = o.getString("name"),
    whatsapp = o.getString("whatsapp"),
    alternate = o.optString("alternate", ""),
    joiningDate = o.getString("joiningDate"),
    type = MembershipType.valueOf(o.getString("type")),
    pkg = PackageType.valueOf(o.getString("pkg")),
    paidMonths = o.getInt("paidMonths"),
    totalMonths = o.optInt("totalMonths", 12),
    photoPath = o.optString("photoPath", "").ifBlank { null }
)

object MemberRepository {
    // Loaded from disk on app start via load(); starts empty until then.
    val members = mutableStateListOf<Member>()

    private fun prefs(): android.content.SharedPreferences =
        AppContextHolder.appContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    /** Call once, e.g. from MainActivity.onCreate, after setting AppContextHolder.appContext. */
    fun load() {
        val json = prefs().getString(KEY_MEMBERS, null) ?: return
        runCatching {
            val arr = JSONArray(json)
            val loaded = (0 until arr.length()).map { memberFromJson(arr.getJSONObject(it)) }
            members.clear()
            members.addAll(loaded)
        }
    }

    private fun persist() {
        val arr = JSONArray()
        members.forEach { arr.put(it.toJson()) }
        prefs().edit().putString(KEY_MEMBERS, arr.toString()).apply()
    }

    fun addMember(member: Member) {
        members.add(member)
        persist()
    }

    fun deleteMember(id: String) {
        members.removeAll { it.id == id }
        persist()
    }

    fun updateMember(updated: Member) {
        val idx = members.indexOfFirst { it.id == updated.id }
        if (idx != -1) {
            members[idx] = updated
            persist()
        }
    }

    fun nextMemberId(): String {
        val today = SimpleDateFormat("yyyyMMdd", Locale.US).format(Date())
        val count = members.size + 1
        return "ES$today${count.toString().padStart(3, '0')}"
    }
}
