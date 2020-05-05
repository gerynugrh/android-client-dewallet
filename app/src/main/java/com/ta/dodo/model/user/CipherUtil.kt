package com.ta.dodo.model.user

import android.util.Base64
import java.nio.charset.StandardCharsets
import java.security.Key
import java.security.KeyFactory
import java.security.PublicKey
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

private const val provider = "AndroidKeyStoreBCWorkaround"

class CipherUtil {
    companion object {
        const val RSA = "RSA/ECB/PKCS1Padding"
        const val AES = "AES/GCM/NoPadding"

        fun decrypt(encryptedBase64: String, key: Key, transformation: String): String {
            val encryptedBytes = decode(encryptedBase64)
            val cipher = Cipher.getInstance(transformation, provider)
            cipher.init(Cipher.DECRYPT_MODE, key)

            return String(cipher.doFinal(encryptedBytes), StandardCharsets.UTF_8)
        }

        fun encrypt(data: String, key: Key, transformation: String): String {
            val cipher = Cipher.getInstance(transformation, provider)
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
            val bytes = decode(data)
            return SecretKeySpec(bytes, 0, bytes.size, "AES")
        }

        fun decodePublicKey(data: String): PublicKey {
            val bytes = decode(data)
            val keySpec = X509EncodedKeySpec(bytes)

            val keyFactory = KeyFactory.getInstance("RSA")
            return keyFactory.generatePublic(keySpec)
        }
    }
}
