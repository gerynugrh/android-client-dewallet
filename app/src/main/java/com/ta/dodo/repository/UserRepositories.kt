package com.ta.dodo.repository

import com.google.gson.Gson
import com.ta.dodo.model.user.CipherUtil
import com.ta.dodo.model.user.User
import com.ta.dodo.service.RetrofitClient
import com.ta.dodo.service.user.request.RegisterUserRequest
import com.ta.dodo.service.user.UserService
import com.ta.dodo.service.user.request.GetPublicKeyRequest
import com.ta.dodo.service.user.request.GetUserDataRequest
import com.ta.dodo.service.user.request.UpdateUserDataRequest
import com.ta.dodo.service.user.response.BaseResponse
import com.ta.dodo.service.user.response.GetUserDataResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mu.KotlinLogging
import java.security.PrivateKey
import java.security.PublicKey

private val logger = KotlinLogging.logger {}

class UserRepositories() {
    private val userService: UserService = RetrofitClient.userService
    private val gson: Gson = Gson()

    companion object {
        private var mToken: String? = null
    }

    private suspend fun getToken() = withContext(Dispatchers.IO) {
        if (mToken == null) {
            val response = userService.getToken("Jim", "Org1")
            logger.info { response }

            mToken = response.token!!
        }
        return@withContext mToken
    }

    suspend fun updateUserData(user: User, publicKey: PublicKey) = withContext(Dispatchers.IO) {
        val token = getToken()
        val auth = "Bearer $token"

        logger.info { auth }
        val dataJson = gson.toJson(user.data)
        val encryptedData = CipherUtil.encrypt(dataJson, publicKey)!!
        val request = UpdateUserDataRequest(user.username, encryptedData)

        userService.updateUserData(request, auth)
    }

    suspend fun getOwnData(username: String, privateKey: PrivateKey) = withContext(Dispatchers.IO) {
        val token = getToken()
        val auth = "Bearer $token"

        val request = GetUserDataRequest(username, username)
        val data: BaseResponse<GetUserDataResponse>?

        data = userService.getUserData(request, auth)
        if (data.data!!.data == "") {
            throw DataNotInitializedException(username)
        }

        val decrypted = CipherUtil.decrypt(data.data!!.data, privateKey)
        logger.info { decrypted }
    }

    suspend fun getPublicKey(username: String) = withContext(Dispatchers.IO) {
        val token = getToken()
        val auth = "Bearer $token"

        val request = GetPublicKeyRequest(username)
        try {
            val data = userService.getPublicKey(request, auth)
            return@withContext data.data!!.publicKey
        } catch (ex: Exception) {
            throw UsernameNotFoundException(username)
        }
    }

    suspend fun create(user: User) = withContext(Dispatchers.IO) {
        val token = getToken()
        val auth = "Bearer $token"

        val request = RegisterUserRequest(user)
        logger.info { auth }
        try {
            userService.register(request, auth)
        } catch (ex: Exception) {
            logger.error { ex.message }
        }
    }

    class DataNotInitializedException(val username: String) : Exception() {
        override val message: String?
            get() = "$username data not initialized"
    }

    class UsernameNotFoundException(val username: String) : Exception() {
        override val message: String?
            get() = "$username not found"
    }
}
