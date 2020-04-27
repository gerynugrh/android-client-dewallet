package com.ta.dodo.service.user.request

import com.google.gson.Gson

class GetUserDataRequest(username: String, owner: String) : BaseRequest("GetUserData") {
    init {
        val arg = getArg(username, owner)
        args = listOf(arg)
    }

    private fun getArg(username: String, owner: String): String {
        val request = Request(username, owner)
        val gson = Gson()

        return gson.toJson(request)
    }

    data class Request(val username: String, val owner: String)
}
