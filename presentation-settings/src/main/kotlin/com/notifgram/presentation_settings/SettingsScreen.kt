package com.notifgram.presentation_settings

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PanToolAlt
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.notifgram.core.common.FirebaseCloudMessaging
import com.notifgram.core.domain.entity.settings.AppSettings
import com.notifgram.core.presentation_core.utils.RequestMultiplePermissions
import com.notifgram.core.presentation_core.utils.ShowPermissionsState

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    val settingsUiState by viewModel.settingsUiState.collectAsStateWithLifecycle()

    ShowSettingsContents(
        settingsUiState,
        toggleShouldEncryptChunks = viewModel::toggleShouldEncryptChunks,
        toggleIsAuthEnabled = viewModel::toggleIsAuthEnabled,
        toggleShouldEncrypt = viewModel::toggleShouldEncrypt,
        toggleFcmUsageEnabled = viewModel::toggleFcmUsageEnabled,
        restartApp = viewModel::restartApp,
        sendEmail = viewModel::sendEmail,
        clearLocalDatabase = viewModel::clearLocalDatabase,
        deleteCachedFilesDirectory = viewModel::deleteCachedFilesDirectory
    )
}

@Composable
private fun ShowSettingsContents(
    uiState: SettingsUiState,
    toggleShouldEncrypt: () -> Unit,
    toggleShouldEncryptChunks: () -> Unit,
    toggleIsAuthEnabled: () -> Unit,
    toggleFcmUsageEnabled: () -> Unit,
    restartApp: (Context) -> Unit,
    sendEmail: (Context, String) -> Unit,
    clearLocalDatabase: () -> Unit,
    deleteCachedFilesDirectory: () -> Unit,
    ) {

    // FOR ASKING FOR PERMISSIONS
    var showPermissionRequest by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 10.dp, end = 10.dp)
            .verticalScroll(rememberScrollState())

    ) {

        when (uiState) {
            is SettingsUiState.Loading -> {}
            is SettingsUiState.Success -> ShowDataStoreSettingsContents(
                uiState.appSettings,
                toggleShouldEncrypt = toggleShouldEncrypt,
                toggleShouldEncryptChunks = toggleShouldEncryptChunks,
                toggleIsAuthEnabled = toggleIsAuthEnabled,
                toggleFcmUsageEnabled = toggleFcmUsageEnabled,
                restartApp = restartApp,
                sendEmail = sendEmail,
                clearLocalDatabase = clearLocalDatabase,
                deleteCachedFilesDirectory = deleteCachedFilesDirectory
            )

        }

        HorizontalDivider()

        Button(
            onClick = { showPermissionRequest = !showPermissionRequest },
            contentPadding = PaddingValues(
                start = 20.dp,
                top = 12.dp,
                end = 20.dp,
                bottom = 12.dp,
            ),
        ) {
            Icon(
                Icons.Filled.PanToolAlt,
                contentDescription = "Request permission",
                modifier = Modifier.size(ButtonDefaults.IconSize),
            )
            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
            Text("Ask for permission")
        }

        if (showPermissionRequest) {
            RequestMultiplePermissions()
            ShowPermissionsState()
        }


    }
}

@Composable
private fun ShowDataStoreSettingsContents(
    appSettings: AppSettings,
    toggleShouldEncrypt: () -> Unit,
    toggleShouldEncryptChunks: () -> Unit,
    toggleIsAuthEnabled: () -> Unit,
    toggleFcmUsageEnabled: () -> Unit,
    restartApp: (Context) -> Unit,
    sendEmail: (Context, String) -> Unit,
    clearLocalDatabase: () -> Unit,
    deleteCachedFilesDirectory: () -> Unit
) {

    Row(modifier = Modifier.padding(20.dp)) {
        Text(
            text = "Chunks encryption",
            modifier = Modifier
                .fillMaxWidth(.8f)
        )
        Spacer(modifier = Modifier.weight(1f))
        Switch(checked = appSettings.shouldEncryptChunks,
            onCheckedChange = { toggleShouldEncryptChunks() })
    }

    HorizontalDivider()

    Column {
        Row(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "Startup Authentication",
                modifier = Modifier
                    .fillMaxWidth(.8f)
            )
            Spacer(modifier = Modifier.weight(1f))

            Switch(enabled = appSettings.isAuthSupported,
                checked = appSettings.isAuthEnabled,
                onCheckedChange = { toggleIsAuthEnabled() })
        }
        if (!appSettings.isAuthSupported)
            Text(text = "authentication is not supported or not enabled on this device.")

    }

    HorizontalDivider()

    Row(modifier = Modifier.padding(20.dp)) {
        Text(
            text = "Cached Files encryption",
            modifier = Modifier
                .fillMaxWidth(.8f)
        )
        Spacer(modifier = Modifier.weight(1f))
        Switch(checked = appSettings.shouldEncrypt, onCheckedChange = { toggleShouldEncrypt() })
    }

    HorizontalDivider()

    Column {
        Row(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "receive data over FCM",
                modifier = Modifier
                    .fillMaxWidth(.8f)
            )
            Spacer(modifier = Modifier.weight(1f))
            Switch(
                checked = appSettings.isFcmUsageEnabled,
                onCheckedChange = { toggleFcmUsageEnabled() })
        }
        Text(text = "receive data through FCM instead of restful API")
    }

    HorizontalDivider(Modifier.padding(20.dp))

    FCMTokenRetrieveAndSendEmail(sendEmail)

    HorizontalDivider(Modifier.padding(20.dp))

    Button(
        colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
        onClick = { deleteCachedFilesDirectory() }
    ) {
        Text("clear cached files")
    }

    HorizontalDivider(Modifier.padding(20.dp))

    Button(
        colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
        onClick = { clearLocalDatabase() }
    ) {
        Text("clear local database")
    }

    HorizontalDivider(Modifier.padding(20.dp))
    val context = LocalContext.current
    Button(onClick = {
        restartApp(context)
    }) {
        Text(text = "restart app")
    }

}

@Composable
fun FCMTokenRetrieveAndSendEmail(sendEmail: (Context, String) -> Unit) {
    val (token, setToken) = remember { mutableStateOf<String?>("Loading token...") }

    // Call the function to get FCM token
    FirebaseCloudMessaging().getFCMToken { newToken ->
        // Update the token state
        setToken(newToken ?: "Token not available")
    }

    Column {
        Text(text = "FCM Token:")
        Text(text = token ?: "Token not available")
    }

    val context = LocalContext.current
    Button(
        enabled = token != null,
        onClick = {
            token?.let { sendEmail(context, it) }
        }
    ) {
        Text("Send Email")
    }
}



//@PreviewLightDark
@Preview
@Composable
private fun PreviewShowSettingsContents() {
    MaterialTheme {

        ShowSettingsContents(
            uiState = SettingsUiState.Success(
                AppSettings(
                    shouldEncrypt = true,
                    isAuthSupported = false,
                    shouldEncryptChunks = false,
                    isAuthEnabled = false,
                    encryptedSqlCipherPassphrase = "",
                    isFcmUsageEnabled = false,
                ),
            ),
            toggleShouldEncrypt = {},
            toggleShouldEncryptChunks = {},
            toggleIsAuthEnabled = {},
            toggleFcmUsageEnabled = {},
            restartApp = {},
            sendEmail = { _, _ -> },
            clearLocalDatabase = {},
            deleteCachedFilesDirectory = {},
        )

    }
}


//@Composable
//fun Screen(
////    uiState: SettingsUiState,
//    onSendButtonClick: (String) -> Unit,
//) {
//    val token = rememberSaveable { mutableStateOf("") }
//
//    Text(text = "Please enter Fcm Token of recipient.")
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(start = 10.dp, end = 10.dp),
//    ) {
//        OutlinedTextField(
//            value = token.value,
//            onValueChange = { token.value = it },
//            label = { Text("Fcm Token") },
////            supportingText = { if (isTempIdInputError) Text(text = errorText) },
//        )
//
//        Button(onClick = { onSendButtonClick(token.value) }) {
//            Text(text = "Send Request")
//        }
//    }
//}