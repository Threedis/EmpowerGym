package com.empowergym.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val PrimaryBlue = Color(0xFF1565C0)
val SecondaryGreen = Color(0xFF2E7D32)
val BackgroundGrey = Color(0xFFF5F7FA)
val CardWhite = Color(0xFFFFFFFF)
val PendingGrey = Color(0xFFB0BEC5)
val WarningAmber = Color(0xFFF9A825)

private val EmpowerColorScheme = lightColorScheme(
    primary = PrimaryBlue,
    secondary = SecondaryGreen,
    background = BackgroundGrey,
    surface = CardWhite,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color(0xFF1A1A1A),
    onSurface = Color(0xFF1A1A1A)
)

// Using system default font family to stand in for Poppins (add Poppins font resources to swap in).
private val EmpowerTypography = Typography()

@Composable
fun EmpowerGymTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = EmpowerColorScheme,
        typography = EmpowerTypography,
        content = content
    )
}
