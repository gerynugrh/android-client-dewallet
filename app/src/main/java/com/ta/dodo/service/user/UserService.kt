package com.ta.dodo.service.user

import com.ta.dodo.service.user.request.GetPublicKeyRequest
import com.ta.dodo.service.user.request.RegisterUserRequest
import com.ta.dodo.service.user.response.BaseResponse
import com.ta.dodo.service.user.response.GetTokenResponse
import com.ta.dodo.service.user.response.RegisterUserResponse
import okhttp3.RequestBody
import retrofit2.http.*

interface UserService {
    @POST("/channels/mychannel/chaincodes/dewallet")
    suspend fun register(
        @Body body: RegisterUserRequest,
        @Header("authorization") authHeader: String
    ) : BaseResponse<RegisterUserResponse>

    @FormUrlEncoded
    @POST("/users")
    suspend fun getToken(
        @Field("username") username: String,
        @Field("orgName") orgName: String
    ) : GetTokenResponse

    @POST("/channels/mychannel/chaincodes/dewallet")
    suspend fun getPublicKey(@Body body: GetPublicKeyRequest)
}