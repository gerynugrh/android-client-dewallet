package com.ta.dodo.service.user.request

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import shadow.com.google.gson.annotations.SerializedName

open class BaseRequest(
    @SerializedName("fcn")
    val fcn: String
) {
    @SerializedName("peers")
    val peers = listOf("peer0.org1.example.com", "peer0.org2.example.com")
    @SerializedName("args")
    var args: List<String> = ArrayList()
}
