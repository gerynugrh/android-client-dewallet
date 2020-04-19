package com.ta.dodo.service.user

import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi

class GetPublicKeyRequest(username: String) : BaseRequest("GetPublicKey") {
    init {
        val arg = getArg(username)
        args = arrayOf(arg)
    }

    private fun getArg(username: String): String {
        val moshi = Moshi.Builder().build()
        val adapter = moshi.adapter(Request::class.java)

        return adapter.toJson(Request(username))
    }

    data class Request(val username: String)
}
