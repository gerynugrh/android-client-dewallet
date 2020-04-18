package com.ta.dodo.service.user

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
open class BaseRequest(
    @Json(name = "fcn")
    val functionName: String
) {
    @Json(name = "peers")
    val peers = arrayOf("peer0.org1.example.com")
    @Json(name = "args")
    var args: Array<String> = emptyArray()
}
