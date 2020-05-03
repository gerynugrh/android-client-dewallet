package com.ta.dodo.service.user.request

import com.google.gson.Gson
import shadow.com.google.gson.annotations.SerializedName

class UpdateUserDataRequest(username: String, data: String) : BaseRequest("UpdateUserData") {
    init {
        val arg = getArg(username, data)
        args = listOf(arg)
    }

    private fun getArg(username: String, data: String) : String {
        val request = Request(
            username = username,
            data = data
        )
        val gson = Gson()

        return gson.toJson(request)
    }

    data class Request (
        @SerializedName("username")
        val username: String,
        @SerializedName("data")
        val data: String
    )
}
