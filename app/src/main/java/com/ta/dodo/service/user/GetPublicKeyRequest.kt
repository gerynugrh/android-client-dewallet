package com.ta.dodo.service.user

import com.google.gson.Gson
import com.ta.dodo.service.user.BaseRequest

class GetPublicKeyRequest(username: String) : BaseRequest("GetPublicKey") {
    init {
        val gson = Gson()
        val arg = gson.toJson(Request(username))
        args = arrayOf(arg)
    }

    data class Request(val username: String)
}
