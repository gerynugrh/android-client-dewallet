package com.ta.dodo.service.user.request

import com.google.gson.Gson
import com.ta.dodo.service.user.request.BaseRequest
import shadow.com.google.gson.annotations.SerializedName

class AddKeyRequest(username: String, owner: String, key: String) : BaseRequest("AddKey") {
    init {
        val arg = getArg(username, owner, key)
        args = listOf(arg)
    }

    private fun getArg(username: String, owner: String, key: String): String {
        val request = Request(
            username = username,
            owner = owner,
            key = key
        )
        val gson = Gson()
        return gson.toJson(request)
    }

    data class Request(
        @SerializedName("username")
        val username: String,
        @SerializedName("owner")
        val owner: String,
        @SerializedName("key")
        val key: String
    )
}