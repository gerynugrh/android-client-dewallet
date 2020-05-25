package com.ta.dodo.service

import com.ta.dodo.service.user.UserService
import com.ta.dodo.service.wallet.WalletService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient {
    companion object {
        private var mUserService: UserService? = null
        private var mWalletService: WalletService? = null

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

        val walletService: WalletService
            get() {
                if (mWalletService == null) {
                    val retrofit = Retrofit.Builder()
                        .baseUrl("http://34.87.91.78:3333")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()

                    mWalletService = retrofit.create(WalletService::class.java)
                }

                return mWalletService!!
            }
    }
}