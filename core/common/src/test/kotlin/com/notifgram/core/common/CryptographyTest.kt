package com.notifgram.core.common

import org.junit.After
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey

class CryptographyTest {

    private lateinit var cryptography: Cryptography
    private lateinit var secretKey: SecretKey

    @Before
    fun setUp() {
        cryptography = Cryptography()

        // Generate a secret key
        val keyGenerator = KeyGenerator.getInstance("AES")
        keyGenerator.init(256)
        secretKey = keyGenerator.generateKey()
    }

    @After
    fun tearDown() {
        // Clean up resources if necessary
    }

    @Test
    fun testEncryptionDecryption() {
        // Test data
        val originalData = "Hello".toByteArray()

        // Encrypt
        val encryptedData = cryptography.encryptByteArray(originalData, secretKey)

        // Decrypt
        val decryptedData = cryptography.decryptByteArray(encryptedData, secretKey)

        // Check if the decrypted data matches the original data
        assertArrayEquals("Decrypted data should match original data", originalData, decryptedData)
    }

    @Test
    fun testGeneratePassphrase() {
        // Generate passphrase
        val passphrase = cryptography.generatePassphrase()

        // Check if the passphrase length matches the expected key size
        assertEquals(
            "Passphrase length should match key size",
            Cryptography.KEY_SIZE / 8,
            passphrase.size
        )
    }
}
