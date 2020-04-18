package com.ta.dodo.service.user

import shadow.com.google.gson.annotations.SerializedName

open class BaseRequest(
    @SerializedName("fcn")
    val functionName: String
) {
    val peers = arrayOf("peer0.org1.example.com")
    var args: Array<String> = emptyArray()
}
