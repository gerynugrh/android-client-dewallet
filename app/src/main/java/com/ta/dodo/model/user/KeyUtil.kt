package com.ta.dodo.model.user

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mu.KotlinLogging
import java.io.FileNotFoundException
import java.security.SecureRandom
import javax.crypto.SecretKey

private val logger = KotlinLogging.logger { }

class KeyUtil private constructor() {
    lateinit var alias: String
    lateinit var secretKey: SecretKey
    val rng: SecureRandom = SecureRandom()
    private val size = 256
    private val secretFileName = "secret.pk"

    companion object {
        lateinit var instance: KeyUtil

        suspend fun build(alias: String, context: Context): KeyUtil {
            val keyGenerator = KeyUtil()
            keyGenerator.alias = alias
            try {
                keyGenerator.load(context)
            } catch (ex: FileNotFoundException) {
                logger.info { ex.message }
            } finally {
                return keyGenerator
            }
        }
    }

    suspend fun load(context: Context) = withContext(Dispatchers.IO) {
        val reader = context.openFileInput(secretFileName).bufferedReader()
        val secret = reader.readLine()
        secretKey = CipherUtil.generateSecretKeyFromSecret(secret)

        logger.info { "secret ${CipherUtil.encode(secretKey.encoded)}" }
    }

    private suspend fun save(secret: String, context: Context) = withContext(Dispatchers.IO) {
        secretKey = CipherUtil.generateSecretKeyFromSecret(secret)
        context.openFileOutput(secretFileName, Context.MODE_PRIVATE).use {
            it.write(secret.toByteArray())
            it.write("\n".toByteArray())
        }
    }

    suspend fun generateSecretKey(context: Context) = withContext(Dispatchers.IO) {
        val key = ByteArray(size)
        rng.nextBytes(key)

        val secretKeyText = CipherUtil.encode(key)
        save(secretKeyText, context)
    }
}