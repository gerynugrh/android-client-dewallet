package com.ta.dodo.service.user.response

open class BaseResponse<T> {
    var success: Boolean? = null
    lateinit var message: String
    var response: T? = null
}