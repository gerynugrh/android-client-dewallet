package com.ta.dodo.service.user.request

import com.google.gson.Gson
import com.ta.dodo.model.user.User
import shadow.com.google.gson.annotations.SerializedName

class RegisterUserRequest(username: String, publicKey: String, ePublicKey: String, sPublicKey: String) : BaseRequest("Register") {
    init {
        val arg = getArg(username, publicKey, ePublicKey, sPublicKey)
        args = listOf(arg)
    }

    private fun getArg(username: String, publicKey: String, ePublicKey: String, sPublicKey: String) : String {
        val request =
            Request(
                username = username,
                publicKey = publicKey,
                ePublicKey = ePublicKey,
                sPublicKey = sPublicKey
            )
        val gson = Gson()

        return gson.toJson(request)
    }

    data class Request(
        @SerializedName("username")
        val username: String,
        @SerializedName("publicKey")
        val publicKey: String,
        @SerializedName("ePublicKey")
        val ePublicKey: String,
        @SerializedName("sPublicKey")
        val sPublicKey: String
    )
}
