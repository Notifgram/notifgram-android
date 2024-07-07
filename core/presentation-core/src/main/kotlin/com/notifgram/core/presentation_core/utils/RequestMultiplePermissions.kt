package com.notifgram.core.presentation_core.utils

import android.Manifest
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.shouldShowRationale


private val neededPermissions= listOf(Manifest.permission.POST_NOTIFICATIONS)


// official documentation: https://google.github.io/accompanist/permissions/#usage
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequestMultiplePermissions() {
    val permissionsState = rememberMultiplePermissionsState(
        permissions = neededPermissions,
    )

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(
        key1 = lifecycleOwner,
        effect = {
            val observer = LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_START) {
                    permissionsState.launchMultiplePermissionRequest()
                }
            }
            lifecycleOwner.lifecycle.addObserver(observer)

            onDispose {
                lifecycleOwner.lifecycle.removeObserver(observer)
            }
        },
    )
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ShowPermissionsState() {

    val permissionsState = rememberMultiplePermissionsState(
        permissions = neededPermissions,
    )

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        permissionsState.permissions.forEach { perm ->
            when (perm.permission) {
                Manifest.permission.POST_NOTIFICATIONS -> {
                    when {
                        perm.status.isGranted -> {
                            Text(
                                text = "post notifications permission granted",
                            )
                        }

                        perm.status.shouldShowRationale -> {
                            Text(
                                text = "post notifications permission is needed",
                            )
                        }

                        perm.isPermanentlyDenied() -> {
                            Text(
                                text = "post notifications permission was permanently" +
                                        "denied. You can enable it in the app" +
                                        "settings.",
                            )
                        }

                    }
                }

            }
        }
    }
}


@ExperimentalPermissionsApi
fun PermissionState.isPermanentlyDenied(): Boolean {
    return !status.shouldShowRationale && !status.isGranted
}