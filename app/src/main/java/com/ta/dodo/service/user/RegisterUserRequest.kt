package com.ta.dodo.service.user

import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.ta.dodo.model.user.User

@JsonClass(generateAdapter = true)
class RegisterUserRequest(user: User) : BaseRequest("Register") {
    init {
        val arg = getArg(user)
        args = arrayOf(arg)
    }

    private fun getArg(user: User) : String {
        val moshi = Moshi.Builder().build()
        val adapter = moshi.adapter(User::class.java)

        return adapter.toJson(user)
    }
}
