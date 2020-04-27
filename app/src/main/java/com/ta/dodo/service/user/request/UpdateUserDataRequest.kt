package com.ta.dodo.service.user.request

import com.google.gson.Gson
import shadow.com.google.gson.annotations.SerializedName

class UpdateUserDataRequest(data: String) : BaseRequest("UpdateUser") {
    init {
        val arg = getArg(data)
        args = listOf(arg)
    }

    private fun getArg(data: String) : String {
        val request = Request(
            data = data
        )
        val gson = Gson()

        return gson.toJson(request)
    }

    data class Request (
        @SerializedName("data")
        val data: String
    )
}
