package com.ta.dodo.service.user.response

import shadow.com.google.gson.annotations.SerializedName

data class GetTokenResponse(
    @SerializedName("success")
    val success: String?,
    @SerializedName("secret")
    val secret: String?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("token")
    val token: String?
)