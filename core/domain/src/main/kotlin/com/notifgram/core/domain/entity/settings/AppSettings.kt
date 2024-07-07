package com.notifgram.core.domain.entity.settings

/**
 * Class related to data store containing user settings about Home screen
 */
data class AppSettings(
//    val showChannelsColumnOnMainScreen: Boolean,
    val isAuthEnabled: Boolean,
    val isAuthSupported: Boolean,
    val shouldEncrypt: Boolean,
    val shouldEncryptChunks: Boolean,
    val isFcmUsageEnabled: Boolean,

    val encryptedSqlCipherPassphrase: String,

)
