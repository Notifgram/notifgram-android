package com.notifgram

import android.app.KeyguardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.notifgram.core.common.FileEncryptionManager
import com.notifgram.core.common.FirebaseCloudMessaging
import com.notifgram.core.common.MyLog.i
import com.notifgram.core.common.loadAllStoredAliases
import com.notifgram.core.domain.entity.settings.AppSettings
import com.notifgram.core.presentation_core.theme.NotifgramTheme
import com.notifgram.core.presentation_core.utils.RequestMultiplePermissions
import com.google.firebase.FirebaseApp
import com.notifgram.MainActivityUiState.Loading
import com.notifgram.MainActivityUiState.Success
import com.notifgram.fcm.FcmTopics
import com.notifgram.presentation.LocalBackPressedDispatcher
import com.notifgram.presentation.navigation.AppModalNavigationDrawer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.security.Security
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var context: Context
    private val viewModel: MainActivityViewModel by viewModels()

    private lateinit var authenticationLauncher: ActivityResultLauncher<Intent>
    private lateinit var keyguardManager: KeyguardManager

    @Inject
    lateinit var appSettings: AppSettings

    @Inject
    lateinit var fileEncryptionManager: FileEncryptionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var uiState: MainActivityUiState by mutableStateOf(Loading)

        // Update the uiState
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState
                    .onEach { uiState = it }
                    .collect()
            }
        }

        context = applicationContext
        handleOnFcmNotificationClick()
        FirebaseApp.initializeApp(/*context=*/this) // Must be called before any Firebase service call

//        i("deleting file...")
//        val uri = "/storage/emulated/0/Download/b.png"
//        val downloadFile = File(uri)
//        downloadFile.createNewFile()
//        downloadFile.delete()

//        val internalFile = File(context.filesDir, "aass")
//        downloadFile.copyTo(internalFile, true)

//        val f = FileDownloaderImpl(context)
//        f.addToQueue("https://www.google.com/images/branding/googlelogo/2x/googlelogo_light_color_272x92dp.png")
//


        //For FCM: registering to a topic of Firebase Cloud Messaging
        i("$TAG appSettings.isFcmUsageEnabled=${appSettings.isFcmUsageEnabled}")
//        if (appSettings.isFcmUsageEnabled)
        FirebaseCloudMessaging().subscribeToTopic(FcmTopics.FCM_SYNC_TOPIC.toString())
//        else
        FirebaseCloudMessaging().subscribeToTopic(FcmTopics.RESTFUL_SYNC_REQUEST_TOPIC.toString())

        viewModel.subscribeToFollowedChannels()
        FirebaseCloudMessaging().logRegistrationToken()

        keyguardManager = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager

        viewModel.updateIsAuthSupported(
            isDeviceSecuritySet(contentResolver, keyguardManager) &&
                    isDeviceSecurityEnabled(keyguardManager)
        )

        authenticationLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                i("auth success")
                setContentForAppModalNavigationDrawer()

            } else {
                i("auth failed")
            }
        }



        i("$TAG Security.getProviders()=${Security.getProviders().toList()}")
        i("$TAG loadAllStoredAliases()=${loadAllStoredAliases().toList()}")

        // Delete all keys stored on keystore
//        val keyStore = KeyStore.getInstance(KEYSTORE_PROVIDER)
//        keyStore.load(null)
//        loadAllStoredAliases().toList().onEach {
//            keyStore.deleteEntry(it)
//        }


//       val text= four.encryptByteArray("hello".toByteArray())
//        i("cryptography4 after encryption =${text.decodeToString()}")
//        val dec= four.decryptByteArray(text)
//        i("cryptography4 after dec =${dec.decodeToString()}")


//        getKeyStore()
//        i("keystore is supported by device")
//        generateSecretKeyAndStoreInKeystore(FILE_ENCRYPTION_KEY_ALIAS)
//

        setContent {
            RequestMultiplePermissions()
            EvaluateUiSettings(uiState)
        }

    }

    @Composable
    private fun EvaluateUiSettings(
        uiState: MainActivityUiState,
    ) {
        when (uiState) {
            Loading -> {}
            is Success -> when (uiState.appSettings.isAuthEnabled) {
                true -> {
                    i("EvaluateUiSettings isAuthEnabled=true ")
                    setContentForAuthentication()
                }

                false -> {
                    i("EvaluateUiSettings isAuthEnabled=false ")
                    setContentForAppModalNavigationDrawer()
                }
            }

        }
    }

    private fun setContentForAppModalNavigationDrawer() {
        setContent {
            NotifgramTheme {
                CompositionLocalProvider(
                    //Makes direction of whole app rtl. Source:https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:compose/material3/material3/integration-tests/material3-catalog/src/main/java/androidx/compose/material3/catalog/library/ui/theme/Theme.kt;l=50?q=CatalogTheme&sq=&ss=androidx%2Fplatform%2Fframeworks%2Fsupport
//                    LocalLayoutDirection provides LayoutDirection.Rtl,
                    // for back button press for closing drawer. Source:D:\SysFiles\SharedFolders\ProgramingProjects\Android-studio\github\compose-samples\Jetchat\app\src\main\java\com\example\compose\jetchat\NavActivity.kt
                    LocalBackPressedDispatcher provides this@MainActivity.onBackPressedDispatcher
                ) {
                    // A surface container using the 'background' color from the theme
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        AppModalNavigationDrawer(
                            //navController = navController,
                            //drawerState = drawerState,
                            //modifier: Modifier = Modifier
                        )

                    }
                }
            }
        }
    }

    private fun setContentForAuthentication() {
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AuthenticateScreen(keyguardManager = keyguardManager) {
                        authenticateUser(
                            keyguardManager,
                            authenticationLauncher
                        )
                    }
                }
            }
        }
    }

    private fun handleOnFcmNotificationClick() {
        i("$TAG handleOnFcmNotificationClick")
        val extras = intent.extras
        if (extras != null) {
        }

    }

    companion object {
        private const val TAG = "MainActivity"
    }
}