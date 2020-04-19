package com.ta.dodo.model.user

import java.io.File
import java.nio.charset.StandardCharsets
import java.security.PublicKey
import javax.crypto.Cipher

private const val provider = "AndroidKeyStoreBCWorkaround"
private const val transformation = "RSA/ECB/PKCS1Padding"

fun extract(encrypted: String, publicKey: PublicKey): ByteArray? {
    val chiper = Cipher.getInstance(transformation, provider)
    chiper.init(Cipher.ENCRYPT_MODE, publicKey)

    return chiper.doFinal(encrypted.toByteArray(StandardCharsets.UTF_8))
}

fun extract(file: File) {

}
