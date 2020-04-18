package com.ta.dodo.service.user

import com.google.gson.Gson
import com.ta.dodo.model.user.User
import com.ta.dodo.service.user.BaseRequest

class RegisterUserRequest(user: User) : BaseRequest("Register") {
    init {
        val gson = Gson()
        val arg = gson.toJson(user)
        args = arrayOf(arg)
    }
}
