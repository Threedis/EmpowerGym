package com.empowergym.app.ui.screens

import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import java.io.File

/**
 * Returns a `launch()` function. Calling it requests camera permission if needed,
 * then opens the device camera and reports the saved photo's file path via [onCaptured].
 */
@Composable
fun rememberCameraCapture(onCaptured: (String) -> Unit): () -> Unit {
    val context = LocalContext.current
    val pendingFile = remember { mutableStateOf<File?>(null) }

    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            pendingFile.value?.absolutePath?.let(onCaptured)
        }
    }

    fun startCapture() {
        val photosDir = File(context.cacheDir, "member_photos").apply { mkdirs() }
        val file = File(photosDir, "photo_${System.currentTimeMillis()}.jpg")
        pendingFile.value = file
        val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
        takePictureLauncher.launch(uri)
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) startCapture()
    }

    return {
        val hasPermission = ContextCompat.checkSelfPermission(
            context, android.Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
        if (hasPermission) startCapture() else permissionLauncher.launch(android.Manifest.permission.CAMERA)
    }
}
