package com.ta.dodo.service.user.request

import com.ta.dodo.model.user.CipherUtil
import com.ta.dodo.service.user.request.BaseRequest
import java.security.PrivateKey

open class PermissionedRequest(fcn: String): BaseRequest(fcn) {
    fun addSignature(privateKey: PrivateKey) {
        val arg = args[0]
        val signature = CipherUtil.sign(arg, privateKey)
        args = listOf(arg, signature)
    }
}