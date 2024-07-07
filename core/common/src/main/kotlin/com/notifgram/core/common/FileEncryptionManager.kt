package com.notifgram.core.common

import com.notifgram.core.common.MyLog.i
import javax.crypto.SecretKey
import javax.inject.Inject

class FileEncryptionManager @Inject constructor(
    private val keystoreManager: KeystoreManager,
    private val cryptography: Cryptography,
) {

    companion object {
        private const val TAG = "FileEncryptionManager"
    }

    /**
     * File encryption and decryption functions.
     */
//    fun encryptFile(file: File) {
//        val secretKey = readSecretKey()
//        val cipher = Cipher.getInstance(CIPHER_TRANSFORMATION)
//        val iv = ByteArray(12)
//        Random.nextBytes(iv)
//        val spec = GCMParameterSpec(128, iv)
//        cipher.init(Cipher.ENCRYPT_MODE, secretKey, spec)
//        val encryptedBytes = cipher.doFinal(file.readBytes())
//        file.writeBytes(encryptedBytes)
//    }

    /**
     * File decryption.
     */
//    fun decryptFile(file: File) {
//        val secretKey = readSecretKey()
//        val cipher = Cipher.getInstance(CIPHER_TRANSFORMATION)
//        val iv = ByteArray(12)
//        val fis = FileInputStream(file)
//        fis.read(iv)
//        val spec = GCMParameterSpec(128, iv)
//        cipher.init(Cipher.DECRYPT_MODE, secretKey, spec)
//        val encryptedBytes = fis.readBytes()
//        fis.close()
//        val decryptedBytes = cipher.doFinal(encryptedBytes)
//        file.writeBytes(decryptedBytes)
//    }

    /**
     * ByteArray encryption.
     */
    fun encryptByteArray(byteArray: ByteArray): ByteArray {
        i("$TAG encryptByteArray()")
        keystoreManager.checkAndGenerateKeyIfNeeded()
        val secretKey: SecretKey = keystoreManager.readSecretKey()
        return cryptography.encryptByteArray(byteArray, secretKey)
    }

    /**
     * ByteArray decryption.
     */
    fun decryptByteArray(byteArray: ByteArray): ByteArray {
        keystoreManager.checkAndGenerateKeyIfNeeded()
        val secretKey = keystoreManager.readSecretKey()
        return cryptography.decryptByteArray(byteArray, secretKey)
    }

    // 7. Test functions.
//    fun runTests() {
//        val testText = "This is a test string."
//        val encryptedBytes = encryptByteArray(testText.toByteArray())
//        val decryptedBytes = decryptByteArray(encryptedBytes)
//        val decryptedText = String(decryptedBytes)
//
//        // Ensure the original and decrypted texts match.
//        assert(testText == decryptedText)
//
//        // Ensure the original and decrypted files match.
//        val testFile = File(context.filesDir, "testFile.txt")
//        testFile.writeText(testText)
//        encryptFile(testFile)
//        decryptFile(testFile)
//        val decryptedFileText = testFile.readText()
//
//        assert(testText == decryptedFileText)
//    }



}
