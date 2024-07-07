package com.notifgram.core.data_local.injection

import android.util.Base64
import com.notifgram.core.common.Cryptography
import com.notifgram.core.common.KeystoreManager
import com.notifgram.core.common.MyLog.e
import com.notifgram.core.common.MyLog.i
import com.notifgram.core.data_datastore.datasource.AppSettingsDataSourceImpl
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.crypto.SecretKey
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class PassphraseManager @Inject constructor(
    private val appSettingsDataSourceImpl: AppSettingsDataSourceImpl,
    private val cryptography: Cryptography,
    @Named("ForPassphraseManager") private val keystoreManager: KeystoreManager
) {

    companion object {
        private const val TAG = "PassphraseManager"
    }

    suspend fun start(): ByteArray {

        if (appSettingsDataSourceImpl.userData.map { it.encryptedSqlCipherPassphrase }
                .first() == "") {
            i("$TAG datastore does not contain passphrase for sqlcipher")
            return generatePassphraseAndStoreInDatastore()
        } else {
            i("$TAG datastore contains passphrase for sqlcipher")

            // If data store contains passphrase but no key exists in keystore.
            // For the case the key in keystore is not deleted on app uninstall.
            if (!keystoreManager.isKeyStoredInKeystore()) {
                e("$TAG !keystoreManager.isKeyStoredInKeystore()")
                return generatePassphraseAndStoreInDatastore()
            }

            val encryptedPassphrase = readEncryptedSqlcipherPassphrase()
            return decryptByteArray(encryptedPassphrase)
        }

    }

    /**
     * returns the generated un-encrypted passphrase
     */
    private suspend fun generatePassphraseAndStoreInDatastore(): ByteArray {
        val passphrase = cryptography.generatePassphrase()
        i("$TAG passphrase=$passphrase")

        val encryptedPassphrase = encryptByteArray(passphrase)
        saveEncryptedSqlcipherPassphraseToDatastore(encryptedPassphrase)
        return passphrase
    }

    private suspend fun readEncryptedSqlcipherPassphrase(): ByteArray {
        i("$TAG readEncryptedSqlcipherPassphrase")

        val encryptedPassphraseString =
            appSettingsDataSourceImpl.userData.map { it.encryptedSqlCipherPassphrase }.first()

        i("$TAG readEncryptedSqlcipherPassphrase encryptedPassphraseString=$encryptedPassphraseString")

        // Convert string to byteArray
        val encryptedPassphraseBytes = Base64.decode(encryptedPassphraseString, Base64.DEFAULT)
        i("$TAG readEncryptedSqlcipherPassphrase encryptedPassphraseBytes=$encryptedPassphraseBytes")

        return encryptedPassphraseBytes
    }

    suspend fun saveEncryptedSqlcipherPassphraseToDatastore(encryptedPassphrase: ByteArray) {
        val encryptedPassphraseString = Base64.encodeToString(encryptedPassphrase, Base64.DEFAULT)
        i("$TAG saveEncryptedSqlcipherPassphraseToDatastore encryptedPassphraseString=$encryptedPassphraseString")

        appSettingsDataSourceImpl.updateEncryptedSqlcipherPassphrase(encryptedPassphraseString)
    }


    /**
     * Functions for ByteArray encryption and decryption.
     */
    private fun encryptByteArray(byteArray: ByteArray): ByteArray {
//        val secretKey = readSecretKey()

        keystoreManager.checkAndGenerateKeyIfNeeded()
        val secretKey: SecretKey = keystoreManager.readSecretKey()
        return cryptography.encryptByteArray(byteArray, secretKey)
    }

    private fun decryptByteArray(byteArray: ByteArray): ByteArray {
        keystoreManager.checkAndGenerateKeyIfNeeded()
        val secretKey: SecretKey = keystoreManager.readSecretKey()
        return cryptography.decryptByteArray(byteArray, secretKey)
        }

}
