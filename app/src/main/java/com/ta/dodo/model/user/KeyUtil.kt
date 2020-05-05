package com.ta.dodo.model.user

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mu.KotlinLogging
import java.math.BigInteger
import java.security.*
import java.util.*
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.security.auth.x500.X500Principal


private val logger = KotlinLogging.logger {}

class KeyUtil private constructor() {
    lateinit var keyStore: KeyStore
    lateinit var alias: String
    lateinit var seed: ByteArray

    companion object {
        suspend fun build(alias: String, seed: ByteArray): KeyUtil {
            val keyGenerator = KeyUtil()
            keyGenerator.alias = alias
            keyGenerator.seed = seed

            keyGenerator.load()

            return keyGenerator
        }
    }

    suspend fun load() {
        withContext(Dispatchers.IO) {
            keyStore = KeyStore.getInstance("AndroidKeyStore")
            keyStore.load(null)
        }
        if (!keyStore.containsAlias(alias)) {
            logger.info { "Generating keypair" }
            generateKeyPair()
        }
    }

    private fun generateKeyPair() {
        logger.info { "Using seed ${String(seed)}" }


        val notBefore = Calendar.getInstance()
        val notAfter = Calendar.getInstance()

        notAfter.add(Calendar.YEAR, 1)

        val randomValue = SecureRandom(seed)

        val keyGenerator =
            KeyPairGenerator.getInstance(KeyProperties.KEY_ALGORITHM_RSA, "AndroidKeyStore")
        keyGenerator.initialize(
            KeyGenParameterSpec.Builder(
                alias, KeyProperties.PURPOSE_DECRYPT or KeyProperties.PURPOSE_ENCRYPT
            )
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1) //  RSA/ECB/PKCS1Padding
                .setKeySize(2048) // *** Replaced: setStartDate
                .setKeyValidityStart(notBefore.time) // *** Replaced: setEndDate
                .setKeyValidityEnd(notAfter.time) // *** Replaced: setSubject
                .setCertificateSubject(X500Principal("CN=test")) // *** Replaced: setSerialNumber
                .setCertificateSerialNumber(BigInteger.ONE)
                .build(), randomValue)
        logger.info { "KeySpec initialized" }

        keyGenerator.genKeyPair()
        logger.info { "Keypair generated" }
    }

    fun generateSecretKey() {
        val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")
        val keyGenParameterSpec =  KeyGenParameterSpec.Builder(alias, KeyProperties.PURPOSE_DECRYPT or KeyProperties.PURPOSE_ENCRYPT)
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .build()

        keyGenerator.init(keyGenParameterSpec)
        keyGenerator.generateKey()
    }

    fun getSecretKey(): SecretKey? {
        return keyStore.getKey(alias, null) as SecretKey
    }

    fun getPrivateKey(): PrivateKey? {
        return keyStore.getKey(alias, null) as PrivateKey
    }

    fun getPublicKey(): PublicKey? {
        return keyStore.getCertificate(alias).publicKey
    }
}