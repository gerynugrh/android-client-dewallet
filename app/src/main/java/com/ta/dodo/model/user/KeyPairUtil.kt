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

class KeyPairUtil private constructor() {
    lateinit var keyStore: KeyStore
    lateinit var encryptionAlias: String
    lateinit var signingAlias: String
    lateinit var seed: ByteArray

    companion object {
        suspend fun build(alias: String, seed: ByteArray): KeyPairUtil {
            val keyGenerator = KeyPairUtil()
            keyGenerator.encryptionAlias = alias + "encrypt"
            keyGenerator.signingAlias = alias + "sign"
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
        if (!keyStore.containsAlias(encryptionAlias) && !keyStore.containsAlias(signingAlias)) {
            logger.info { "Generating keypair" }
            generateEncryptionKeyPair()
            generateSigningKeyPair()
        }
    }

    private fun generateEncryptionKeyPair() {
        logger.info { "Using seed ${String(seed)}" }


        val notBefore = Calendar.getInstance()
        val notAfter = Calendar.getInstance()

        notAfter.add(Calendar.YEAR, 1)

        val randomValue = SecureRandom(seed)

        val keyGenerator =
            KeyPairGenerator.getInstance(KeyProperties.KEY_ALGORITHM_RSA, "AndroidKeyStore")
        keyGenerator.initialize(
            KeyGenParameterSpec.Builder(
                encryptionAlias, KeyProperties.PURPOSE_DECRYPT or KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_SIGN
            )
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1) //  RSA/ECB/PKCS1Padding
                .setUserAuthenticationRequired(false)
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

    private fun generateSigningKeyPair() {
        logger.info { "Using seed ${String(seed)}" }


        val notBefore = Calendar.getInstance()
        val notAfter = Calendar.getInstance()

        notAfter.add(Calendar.YEAR, 1)

        val randomValue = SecureRandom(seed)

        val keyGenerator =
            KeyPairGenerator.getInstance(KeyProperties.KEY_ALGORITHM_RSA, "AndroidKeyStore")
        keyGenerator.initialize(
            KeyGenParameterSpec.Builder(
                signingAlias, KeyProperties.PURPOSE_SIGN or KeyProperties.PURPOSE_VERIFY
            )
                .setSignaturePaddings(KeyProperties.SIGNATURE_PADDING_RSA_PKCS1) //  RSA/ECB/PKCS1Padding
                .setDigests(KeyProperties.DIGEST_SHA256)
                .setUserAuthenticationRequired(false)
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

    fun getEncryptionKey(): Pair<PrivateKey, PublicKey> {
        val privateKey = keyStore.getKey(encryptionAlias, null) as PrivateKey
        val publicKey = keyStore.getCertificate(encryptionAlias).publicKey

        return Pair(privateKey, publicKey)
    }

    fun getSigningKey(): Pair<PrivateKey, PublicKey> {
        val privateKey = keyStore.getKey(signingAlias, null) as PrivateKey
        val publicKey = keyStore.getCertificate(signingAlias).publicKey

        return Pair(privateKey, publicKey)
    }

    fun getPrivateKey(): PrivateKey? {
        return keyStore.getKey(encryptionAlias, null) as PrivateKey
    }

    fun getPublicKey(): PublicKey? {
        return keyStore.getCertificate(encryptionAlias).publicKey
    }
}