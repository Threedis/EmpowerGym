package com.empowergym.app.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight

/**
 * Standard top bar used on every screen: always shows the "Empower Gym" app name,
 * with the current page name as a subtitle underneath.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmpowerTopBar(
    pageTitle: String? = null,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable androidx.compose.foundation.layout.RowScope.() -> Unit = {}
) {
    TopAppBar(
        title = {
            Column {
                Text("Empower Gym", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                if (pageTitle != null) {
                    Text(pageTitle, style = MaterialTheme.typography.labelMedium, color = Color.Gray)
                }
            }
        },
        navigationIcon = navigationIcon,
        actions = actions
    )
}
