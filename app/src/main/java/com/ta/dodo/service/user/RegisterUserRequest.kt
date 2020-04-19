package com.ta.dodo.service.user

import com.google.gson.Gson
import com.ta.dodo.model.user.User
import shadow.com.google.gson.annotations.SerializedName

class RegisterUserRequest(user: User, data: String) : BaseRequest("Register") {
    init {
        val arg = getArg(user, data)
        args = arrayOf(arg)
    }

    private fun getArg(user: User, data: String) : String {
        val request = Request(
            username = user.username,
            publicKey = user.publicKey,
            data = data
        )
        val gson = Gson()

        return gson.toJson(request)
    }

    data class Request(
        @SerializedName("username")
        val username: String,
        @SerializedName("publicKey")
        val publicKey: String,
        @SerializedName("data")
        val data: String
    )
}
