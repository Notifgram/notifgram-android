package com.notifgram.core.common

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import com.notifgram.core.common.MyLog.i
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.security.KeyStore
import java.util.Enumeration
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object KeystoreModule {

    private const val SQLCIPHER_KEY_ALIAS = "sqlcipher_passphrase_encryption_key"
    private const val FILE_ENCRYPTION_KEY_ALIAS = "file_encryption_alias_key"

    @Provides
    @Singleton
    @Named("ForFileEncryptionManager")
    fun provideKeystoreManagerForFileEncryptionManager(): KeystoreManager {
        return KeystoreManager(FILE_ENCRYPTION_KEY_ALIAS)
    }

    @Provides
    @Singleton
    @Named("ForPassphraseManager")
    fun provideKeystoreManagerForPassphraseManager(): KeystoreManager {
        return KeystoreManager(SQLCIPHER_KEY_ALIAS)
    }

}

const val KEYSTORE_PROVIDER = "AndroidKeyStore"

class KeystoreManager @Inject constructor(
    private val keyAlias: String,
) {
    companion object {
        private const val TAG = "KeystoreManager"
    }

    /**
     *  Checks if the key is stored in the keystore. If not, generate and store the key.
     */
    fun checkAndGenerateKeyIfNeeded() {
        if (!isKeyStoredInKeystore()) {
            val secretKey = generateSecretKey()
            storeSecretKey(secretKey)
        }
    }

    /**
     * Reads the key from the keystore.
     */
    fun readSecretKey(): SecretKey {
        val keyStore = KeyStore.getInstance(KEYSTORE_PROVIDER)
        keyStore.load(null)
        return keyStore.getKey(keyAlias, null) as SecretKey
    }

    /**
     * Stores the generated key in the keystore.
     */
    private fun storeSecretKey(secretKey: SecretKey) {
        val keyStore = KeyStore.getInstance(KEYSTORE_PROVIDER)
        keyStore.load(null)
        val entry = KeyStore.SecretKeyEntry(secretKey)
        keyStore.setEntry(keyAlias, entry, null)
    }

    /**
     *  Generates a secure key for encryption.
     */
    fun generateSecretKey(): SecretKey {
        i("$TAG generateSecretKey")
        val keyGenerator =
            KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, KEYSTORE_PROVIDER)
        val keyGenParameterSpec = KeyGenParameterSpec.Builder(
            keyAlias,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .build()
        keyGenerator.init(keyGenParameterSpec)
        return keyGenerator.generateKey()
    }

    /**
     * checks if the key is stored in the keystore.
     */
    fun isKeyStoredInKeystore(): Boolean {
        val keyStore = KeyStore.getInstance(KEYSTORE_PROVIDER)
        keyStore.load(null)
        return keyStore.containsAlias(keyAlias)
    }

    /**
     * Deletes key from keystore.
     */
    fun deleteEntry() {
        val keyStore = KeyStore.getInstance(KEYSTORE_PROVIDER)
        keyStore.load(null)
        keyStore.deleteEntry(keyAlias)
    }


}

/**
 * source: https://developer.android.com/privacy-and-security/keystore#WorkingWithKeyStoreEntries
 * Load the Android KeyStore instance using the
 * AndroidKeyStore provider to list the currently stored entries.
 */
fun loadAllStoredAliases(): Enumeration<String> {

    val ks: KeyStore = KeyStore.getInstance(KEYSTORE_PROVIDER).apply {
        load(null)
    }
    val aliases: Enumeration<String> = ks.aliases()
    return aliases
}

/**
 * Source: https://developer.android.com/reference/kotlin/java/security/KeyStore
 * Retrieves the number of entries in this keystore.
 */
fun getNumberOfEntriesInKeystore(): Int {
    val ks: KeyStore = KeyStore.getInstance(KEYSTORE_PROVIDER).apply {
        load(null)
    }
    return ks.size()
}