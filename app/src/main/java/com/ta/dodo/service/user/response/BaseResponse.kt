package com.ta.dodo.service.user.response

import shadow.com.google.gson.annotations.SerializedName

open class BaseResponse<T> {
    var success: Boolean? = null
    lateinit var message: String
    var data: T? = null
}