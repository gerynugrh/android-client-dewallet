package com.ta.dodo.model.user

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mu.KotlinLogging
import java.math.BigInteger
import java.security.*
import java.util.*
import javax.security.auth.x500.X500Principal


private val logger = KotlinLogging.logger {}

class KeyGenerator private constructor() {
    lateinit var keyStore: KeyStore
    lateinit var alias: String
    lateinit var seed: ByteArray

    companion object {
        suspend fun build(alias: String, seed: ByteArray): KeyGenerator {
            val keyGenerator = KeyGenerator()
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

        val keySpec =
            KeyPairGenerator.getInstance(KeyProperties.KEY_ALGORITHM_RSA, "AndroidKeyStore")
        keySpec.initialize(
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

        keySpec.genKeyPair()
        logger.info { "Keypair generated" }
    }

    fun getPrivateKey(): PrivateKey? {
        return keyStore.getKey(alias, null) as PrivateKey
    }

    fun getPublicKey(): PublicKey? {
        return keyStore.getCertificate(alias).publicKey
    }
}