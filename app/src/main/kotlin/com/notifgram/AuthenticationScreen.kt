package com.notifgram

import android.app.KeyguardManager
import android.content.ContentResolver
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun AuthenticateScreen(keyguardManager: KeyguardManager, authenticate: () -> Unit) {
    var isAuthenticationEnabled by remember { mutableStateOf(false) }

    // Check if authentication is enabled before showing the button
    if (!isAuthenticationEnabled) {
        isAuthenticationEnabled = isDeviceSecurityEnabled(keyguardManager)
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Show the authenticate button only if authentication is enabled
            if (isAuthenticationEnabled) {
                Button(onClick = { authenticate() }) {
//                    Icon(Icons.Default.Fingerprint, contentDescription = null)
                    Text(text = "Authenticate")
                }
            } else {
                Text(text = "Device security not enabled")
            }
        }
    }
}

fun authenticateUser(
    keyguardManager: KeyguardManager,
    authenticationLauncher: ActivityResultLauncher<Intent>
) {
//    val keyguardManager = getSystemService(context.KEYGUARD_SERVICE) as KeyguardManager
    val authenticationIntent: Intent =
        keyguardManager.createConfirmDeviceCredentialIntent(
            "Authentication required",
            "Please authenticate to continue"
        )

    authenticationLauncher.launch(authenticationIntent)
}

fun isDeviceSecuritySet(
    contentResolver: ContentResolver,
    keyguardManager: KeyguardManager
): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//        val keyguardManager = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        keyguardManager.isDeviceSecure
    } else {
        val lockPatternEnable: String =
            Settings.Secure.getString(contentResolver, Settings.Secure.LOCK_PATTERN_ENABLED)
                ?: "0"
        lockPatternEnable != "0"
    }
}

fun isDeviceSecurityEnabled(keyguardManager: KeyguardManager): Boolean {
//    val keyguardManager = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        keyguardManager.isDeviceSecure
    } else {
        keyguardManager.isKeyguardSecure
    }
}