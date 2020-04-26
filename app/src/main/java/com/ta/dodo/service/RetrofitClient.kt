package com.ta.dodo.service

import com.ta.dodo.service.user.UserService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient {
    companion object {
        private var mUserService: UserService? = null

        val userService: UserService
            get() {
                if (mUserService == null) {
                    val retrofit = Retrofit.Builder()
                        .baseUrl("http://34.87.91.78:4000")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()

                    mUserService = retrofit.create(UserService::class.java)
                }

                return mUserService!!
            }
    }
}