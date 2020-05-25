package com.ta.dodo.service.wallet

import retrofit2.http.Body
import retrofit2.http.POST

interface WalletService {
    @POST("/wallet")
    suspend fun create(@Body body: CreateWallet.Request): CreateWallet.Response
}