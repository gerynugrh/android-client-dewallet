package com.ta.dodo.service

import retrofit2.http.Body
import retrofit2.http.POST

interface UserService {
    @POST("/channels/mychannel/chaincodes/dewallet")
    suspend fun register()

    @POST("/channels/mychannel/chaincodes/dewallet")
    suspend fun getPublicKey()
}