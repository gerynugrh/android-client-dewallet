package com.ta.dodo.model.user

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mu.KotlinLogging
import java.security.KeyStore
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey

private val logger = KotlinLogging.logger {  }

class KeyUtil private constructor() {
    lateinit var keyStore: KeyStore
    lateinit var alias: String

    companion object {
        suspend fun build(alias: String): KeyUtil {
            val keyGenerator = KeyUtil()
            keyGenerator.alias = alias
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
            generateSecretKey()
        }
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
}