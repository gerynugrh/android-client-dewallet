package com.ta.dodo.model.user

import android.util.Base64
import mu.KotlinLogging
import java.nio.charset.StandardCharsets
import java.security.*
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

private const val provider = "AndroidKeyStoreBCWorkaround"
private val logger = KotlinLogging.logger {  }

class CipherUtil {
    companion object {
        const val RSA = "RSA/ECB/PKCS1Padding"
        const val AES = "AES/GCM/NoPadding"
        val salt = "salt".toByteArray()
        val ivSpec = IvParameterSpec("iv".toByteArray())

        fun decrypt(encryptedBase64: String, key: Key, transformation: String): String {
            val encryptedBytes = decode(encryptedBase64)
            val cipher = Cipher.getInstance(transformation, provider)
            cipher.init(Cipher.DECRYPT_MODE, key)

            return String(cipher.doFinal(encryptedBytes), StandardCharsets.UTF_8)
        }

        fun decryptWithoutProvider(encryptedBase64: String, key: Key, transformation: String): String {
            val encryptedBytes = decode(encryptedBase64)
            val cipher = Cipher.getInstance(transformation)
            cipher.init(Cipher.DECRYPT_MODE, key)

            return String(cipher.doFinal(encryptedBytes), StandardCharsets.UTF_8)
        }

        fun encrypt(data: String, key: Key, transformation: String): String {
            val cipher = Cipher.getInstance(transformation, provider)
            cipher.init(Cipher.ENCRYPT_MODE, key)

            val encryptedBytes = cipher.doFinal(data.toByteArray(StandardCharsets.UTF_8))
            return encode(encryptedBytes)
        }

        fun encryptWithoutProvider(data: String, key: Key, transformation: String): String {
            val cipher = Cipher.getInstance(transformation)
            cipher.init(Cipher.ENCRYPT_MODE, key)

            val encryptedBytes = cipher.doFinal(data.toByteArray(StandardCharsets.UTF_8))
            return encode(encryptedBytes)
        }

        fun encode(data: ByteArray): String {
            return Base64.encodeToString(data, Base64.DEFAULT)
        }

        fun decode(data: String): ByteArray {
            return Base64.decode(data, Base64.DEFAULT)
        }

        fun decodeSecretKey(data: String): SecretKey {
            val pbKeySpec = PBEKeySpec(data.toCharArray(), salt, 1024, 256)
            val secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1")
            val keyBytes = secretKeyFactory.generateSecret(pbKeySpec).encoded

            return SecretKeySpec(keyBytes, "AES")
        }

        fun decodePublicKey(data: String): PublicKey {
            val bytes = decode(data)
            val keySpec = X509EncodedKeySpec(bytes)

            val keyFactory = KeyFactory.getInstance("RSA")
            return keyFactory.generatePublic(keySpec)
        }
    }
}
