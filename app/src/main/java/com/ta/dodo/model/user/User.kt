package com.ta.dodo.model.user

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.ta.dodo.model.wallet.Wallet
import java.util.*

class User(val username: String, private val wallet: Wallet) {

    lateinit var data: Data
    val publicKey: String = wallet.getAccountId()

    data class Data(
        @Json(name = "address")
        val address: String?,
        @Json(name = "identityNumber")
        val identityNumber: String?,
        @Json(name = "dateOfBirth")
        val dateOfBirth: Date?
    )

    class DataBuilder {
        private var address: String? = null
        private var identityNumber: String? = null
        private var dateOfBirth: Date? = null

        fun addAddress(address: String): DataBuilder {
            this.address = address
            return this
        }

        fun addIdentityNumber(identityNumber: String): DataBuilder {
            this.identityNumber = identityNumber
            return this
        }

        fun addDateOfBirth(dateOfBirth: Date): DataBuilder {
            this.dateOfBirth = dateOfBirth
            return this
        }

        fun build(): Data {
            return Data(
                address,
                identityNumber,
                dateOfBirth
            )
        }
    }

    fun getEncryptedData() {

    }
}
