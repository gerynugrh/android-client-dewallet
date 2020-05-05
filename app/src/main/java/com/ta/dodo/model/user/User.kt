package com.ta.dodo.model.user

import shadow.com.google.gson.annotations.SerializedName
import java.security.PublicKey
import javax.crypto.SecretKey

class User(val username: String, val publicKey: String, val ePublicKey: PublicKey) {

    lateinit var data: Data

    data class Data(
        @SerializedName("phoneNumber")
        val phoneNumber: String?,
        @SerializedName("identityNumber")
        val identityNumber: String?,
        @SerializedName("fullName")
        val fullName: String?,
        @SerializedName("email")
        val email: String?
    )

    suspend fun generateSecretKey() {
        val keyUtil = KeyUtil.build(username)
    }

    suspend fun getSecretKey(): SecretKey {
        val keyGenerator = KeyUtil.build(username)
        return keyGenerator.getSecretKey()!!
    }

    class DataBuilder {
        private var phoneNumber: String? = null
        private var identityNumber: String? = null
        private var fullName: String? = null
        private var email: String? = null

        fun addPhoneNumber(phoneNumber: String): DataBuilder {
            this.phoneNumber = phoneNumber
            return this
        }

        fun addIdentityNumber(identityNumber: String): DataBuilder {
            this.identityNumber = identityNumber
            return this
        }

        fun addFullName(fullName: String): DataBuilder {
            this.fullName = fullName
            return this
        }

        fun addEmail(email: String): DataBuilder {
            this.email = email
            return this
        }

        fun build(): Data {
            return Data(
                phoneNumber = phoneNumber,
                identityNumber = identityNumber,
                fullName = fullName,
                email = email
            )
        }
    }
}
