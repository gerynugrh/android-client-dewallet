package com.ta.dodo.service.user

import com.ta.dodo.service.user.request.GetPublicKeyRequest
import com.ta.dodo.service.user.request.GetUserDataRequest
import com.ta.dodo.service.user.request.RegisterUserRequest
import com.ta.dodo.service.user.response.*
import okhttp3.RequestBody
import retrofit2.http.*

interface UserService {
    @POST("/channels/mychannel/chaincodes/dewallet")
    suspend fun register(
        @Body body: RegisterUserRequest,
        @Header("authorization") auth: String
    ): BaseResponse<RegisterUserResponse>

    @FormUrlEncoded
    @POST("/users")
    suspend fun getToken(
        @Field("username") username: String,
        @Field("orgName") orgName: String
    ): GetTokenResponse

    @POST("/channels/mychannel/chaincodes/dewallet")
    suspend fun getPublicKey(@Body body: GetPublicKeyRequest): BaseResponse<GetPublicKeyResponse>

    @POST("/channels/mychannel/chaincodes/dewallet")
    suspend fun getUserData(
        @Body body: GetUserDataRequest,
        @Header("authorization") auth: String
    ): BaseResponse<GetUserDataResponse>
}