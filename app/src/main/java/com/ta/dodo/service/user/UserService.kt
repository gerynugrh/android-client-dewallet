package com.ta.dodo.service.user

import com.ta.dodo.service.user.GetPublicKeyRequest
import com.ta.dodo.service.user.RegisterUserRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface UserService {
    @POST("/channels/mychannel/chaincodes/dewallet")
    suspend fun register(@Body body: RegisterUserRequest)

    @POST("/channels/mychannel/chaincodes/dewallet")
    suspend fun getPublicKey(@Body body: GetPublicKeyRequest)
}