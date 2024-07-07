package com.notifgram.core.common

import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

class Cryptography {

    companion object {
        private const val TAG = "Cryptography"
        internal const val KEY_SIZE = 256
        private const val CIPHER_TRANSFORMATION = "AES/GCM/NoPadding"
    }

    /**
     * ByteArray encryption.
     */
    fun encryptByteArray(byteArray: ByteArray, secretKey: SecretKey): ByteArray {
        val cipher = Cipher.getInstance(CIPHER_TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)

        // Generate IV (Initialization Vector)
        val iv = cipher.iv

        // Encrypt the data
        val encryptedData = cipher.doFinal(byteArray)

        // Concatenate IV and encrypted data
        val encryptedByteArray = ByteArray(iv.size + encryptedData.size)
        System.arraycopy(iv, 0, encryptedByteArray, 0, iv.size)
        System.arraycopy(encryptedData, 0, encryptedByteArray, iv.size, encryptedData.size)

        // Encrypt the data
        return encryptedByteArray
    }

    /**
     * ByteArray decryption.
     */
    fun decryptByteArray(byteArray: ByteArray, secretKey: SecretKey): ByteArray {
        val cipher = Cipher.getInstance(CIPHER_TRANSFORMATION)

        val iv = GCMParameterSpec(128, byteArray.copyOfRange(0, 12))
        cipher.init(Cipher.DECRYPT_MODE, secretKey, iv)

        return cipher.doFinal(byteArray.copyOfRange(12, byteArray.size))

    }

    /**
     * Generates a passphrase for SqlCipher
     */
    fun generatePassphrase(): ByteArray {
        val passphrase = ByteArray(KEY_SIZE / 8)
        val secureRandom = SecureRandom()
        secureRandom.nextBytes(passphrase)
        return passphrase
    }
}