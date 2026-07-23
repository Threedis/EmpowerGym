package com.empowergym.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.empowergym.app.ui.screens.*
import com.empowergym.app.ui.theme.EmpowerGymTheme

sealed class Screen(val route: String, val label: String) {
    object Dashboard : Screen("dashboard", "Dashboard")
    object Members : Screen("members", "Members")
    object Fees : Screen("fees", "Fees")
    object Reports : Screen("reports", "Reports")
    object Settings : Screen("settings", "Settings")
    object Register : Screen("register", "Register")
    object MemberDetails : Screen("member/{memberId}", "Member")
    object FeeUpdate : Screen("feeUpdate/{memberId}", "Fee Update")
    object WhatsAppConfirm : Screen("whatsappConfirm", "Confirm")
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EmpowerGymTheme {
                EmpowerGymApp()
            }
        }
    }
}

@Composable
fun EmpowerGymApp() {
    val navController = rememberNavController()
    val bottomItems = listOf(
        Triple(Screen.Dashboard.route, "Dashboard", Icons.Filled.Home),
        Triple(Screen.Members.route, "Members", Icons.Filled.Person),
        Triple(Screen.Fees.route, "Fees", Icons.Filled.AttachMoney),
        Triple(Screen.Reports.route, "Reports", Icons.Filled.BarChart),
        Triple(Screen.Settings.route, "Settings", Icons.Filled.Settings)
    )

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            if (currentRoute in bottomItems.map { it.first }) {
                NavigationBar {
                    bottomItems.forEach { (route, label, icon) ->
                        NavigationBarItem(
                            selected = currentRoute == route,
                            onClick = {
                                navController.navigate(route) {
                                    popUpTo(Screen.Dashboard.route)
                                    launchSingleTop = true
                                }
                            },
                            icon = { Icon(icon, contentDescription = label) },
                            label = { Text(label) }
                        )
                    }
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Dashboard.route,
            modifier = Modifier.padding(padding)
        ) {
            composable(Screen.Dashboard.route) {
                DashboardScreen(
                    onRegisterMember = { navController.navigate(Screen.Register.route) },
                    onCollectFees = { navController.navigate(Screen.Members.route) }
                )
            }
            composable(Screen.Members.route) {
                MembersListScreen(
                    onOpenMember = { memberId ->
                        navController.navigate("member/$memberId")
                    },
                    onAddMember = { navController.navigate(Screen.Register.route) }
                )
            }
            composable(Screen.Fees.route) {
                MembersListScreen(
                    onOpenMember = { memberId -> navController.navigate("member/$memberId") },
                    onAddMember = { navController.navigate(Screen.Register.route) }
                )
            }
            composable(Screen.Reports.route) { ReportsScreen() }
            composable(Screen.Settings.route) { SettingsScreen() }
            composable(Screen.Register.route) {
                RegisterMemberScreen(
                    onBack = { navController.popBackStack() },
                    onRegistered = { navController.popBackStack() }
                )
            }
            composable(Screen.MemberDetails.route) { entry ->
                val memberId = entry.arguments?.getString("memberId") ?: ""
                MemberDetailsScreen(
                    memberId = memberId,
                    onBack = { navController.popBackStack() },
                    onUpdateFees = { navController.navigate("feeUpdate/$memberId") }
                )
            }
            composable(Screen.FeeUpdate.route) { entry ->
                val memberId = entry.arguments?.getString("memberId") ?: ""
                FeeUpdateScreen(
                    memberId = memberId,
                    onBack = { navController.popBackStack() },
                    onUpdated = { navController.navigate(Screen.WhatsAppConfirm.route) }
                )
            }
            composable(Screen.WhatsAppConfirm.route) {
                WhatsAppConfirmScreen(
                    onDone = {
                        navController.navigate(Screen.Dashboard.route) {
                            popUpTo(Screen.Dashboard.route) { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}
