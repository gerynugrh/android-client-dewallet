package com.ta.dodo.service.user.request

import com.google.gson.Gson
import com.ta.dodo.model.user.User
import shadow.com.google.gson.annotations.SerializedName

class RegisterUserRequest(user: User) : BaseRequest("Register") {
    init {
        val arg = getArg(user)
        args = listOf(arg)
    }

    private fun getArg(user: User) : String {
        val request =
            Request(
                username = user.username,
                publicKey = user.publicKey,
                ePublicKey = user.ePublicKey
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
