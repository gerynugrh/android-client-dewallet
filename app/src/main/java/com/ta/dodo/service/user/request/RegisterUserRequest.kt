package com.ta.dodo.service.user.request

import com.google.gson.Gson
import com.ta.dodo.model.user.User
import shadow.com.google.gson.annotations.SerializedName

class RegisterUserRequest(username: String, publicKey: String, ePublicKey: String) : BaseRequest("Register") {
    init {
        val arg = getArg(username, publicKey, ePublicKey)
        args = listOf(arg)
    }

    private fun getArg(username: String, publicKey: String, ePublicKey: String) : String {
        val request =
            Request(
                username = username,
                publicKey = publicKey,
                ePublicKey = ePublicKey
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
        val ePublicKey: String
    )
}
