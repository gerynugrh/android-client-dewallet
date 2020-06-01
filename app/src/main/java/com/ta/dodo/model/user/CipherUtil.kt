package com.ta.dodo.model.user

import android.R.attr
import android.util.Base64
import com.ta.dodo.toHexString
import mu.KotlinLogging
import java.lang.StringBuilder
import java.nio.charset.StandardCharsets
import java.security.*
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec
import kotlin.experimental.and


private const val provider = "AndroidKeyStoreBCWorkaround"
private val logger = KotlinLogging.logger { }

class CipherUtil {
    companion object {
        const val RSA = "RSA/ECB/PKCS1Padding"
        const val AES = "AES/CBC/PKCS7Padding"
        val salt = "salt".toByteArray()
        val ivSpec = IvParameterSpec("8tv56iUSZZiQg7Qb".toByteArray())
        private val random = SecureRandom()

        fun decrypt(encryptedBase64: String, key: Key, transformation: String): String {
            val encryptedBytes = decode(encryptedBase64)
            val cipher = Cipher.getInstance(transformation, provider)
            cipher.init(Cipher.DECRYPT_MODE, key)

            return String(cipher.doFinal(encryptedBytes), StandardCharsets.UTF_8)
        }

        fun decryptWithoutProvider(
            encryptedBase64: String,
            key: Key,
            transformation: String
        ): String {
            val encryptedBytes = decode(encryptedBase64)
            val cipher = Cipher.getInstance(transformation)
            if (transformation == AES) {
                val ivByte = encryptedBytes.copyOfRange(0, 16)
                logger.info { encode(ivByte) }
                cipher.init(Cipher.DECRYPT_MODE, key, IvParameterSpec(ivByte))
            } else {
                cipher.init(Cipher.DECRYPT_MODE, key)
            }

            return String(
                cipher.doFinal(encryptedBytes.copyOfRange(16, encryptedBytes.size)),
                StandardCharsets.UTF_8
            )
        }

        fun encrypt(data: String, key: Key, transformation: String): String {
            val cipher = Cipher.getInstance(transformation, provider)
            cipher.init(Cipher.ENCRYPT_MODE, key)

            val encryptedBytes = cipher.doFinal(data.toByteArray(StandardCharsets.UTF_8))
            return encode(encryptedBytes)
        }

        fun encryptWithoutProvider(data: String, key: Key, transformation: String): String {
            val cipher = Cipher.getInstance(transformation)
            val ivByte = generateIvBytes()
            if (transformation == AES) {
                logger.info { encode(ivByte) }
                cipher.init(Cipher.ENCRYPT_MODE, key, IvParameterSpec(ivByte))
            } else {
                cipher.init(Cipher.ENCRYPT_MODE, key)
            }

            val encryptedBytes = ivByte + cipher.doFinal(data.toByteArray(StandardCharsets.UTF_8))
            return encode(encryptedBytes)
        }

        private fun generateIvBytes(): ByteArray {
            val ivBytes = ByteArray(16)
            random.nextBytes(ivBytes)

            return ivBytes
        }

        fun encode(data: ByteArray): String {
            return Base64.encodeToString(data, Base64.DEFAULT)
        }

        fun decode(data: String): ByteArray {
            return Base64.decode(data, Base64.DEFAULT)
        }

        fun generateSecretKeyFromSecret(data: String): SecretKey {
            val pbKeySpec = PBEKeySpec(data.toCharArray(), salt, 1024, 256)
            val secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1")
            val keyBytes = secretKeyFactory.generateSecret(pbKeySpec).encoded

            return SecretKeySpec(keyBytes, "AES")
        }

        fun decodeSecretKey(keyBytes: ByteArray): SecretKey {
            return SecretKeySpec(keyBytes, "AES")
        }

        fun decodePublicKey(data: String): PublicKey {
            val bytes = decode(data)
            val keySpec = X509EncodedKeySpec(bytes)

            val keyFactory = KeyFactory.getInstance("RSA")
            return keyFactory.generatePublic(keySpec)
        }

        fun hash(data: String): String {
            var digest: MessageDigest? = null
            return try {
                digest = MessageDigest.getInstance("SHA-256")
                digest.reset()
                val bytes = digest.digest(data.toByteArray())
                bytes.toHexString()
            } catch (e1: NoSuchAlgorithmException) {
                // TODO Auto-generated catch block
                e1.printStackTrace()
                ""
            }
        }

        fun sign(data: String, privateKey: PrivateKey): String {
            val signer = Signature.getInstance("SHA256WithRSA")
            signer.initSign(privateKey)
            signer.update(data.toByteArray())

            return signer.sign().toHexString()
        }
    }
}
