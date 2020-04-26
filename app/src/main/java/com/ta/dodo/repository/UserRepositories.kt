package com.ta.dodo.repository

import com.google.gson.Gson
import com.ta.dodo.model.user.CipherUtil
import com.ta.dodo.model.user.User
import com.ta.dodo.service.RetrofitClient
import com.ta.dodo.service.user.request.RegisterUserRequest
import com.ta.dodo.service.user.UserService
import com.ta.dodo.service.user.request.GetUserDataRequest
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

    suspend fun update(user: User) {

    }

    suspend fun getData(username: String, privateKey: PrivateKey) = withContext(Dispatchers.IO) {
        val token: String?
        try {
            token = getToken()
        } catch (ex: Exception) {
            logger.error { ex.message }
            throw DataNotInitializedException(username)
        }

        val auth = "Bearer $token"

        val request = GetUserDataRequest(username)
        val data = userService.getUserData(request, auth)

        val decrypted = CipherUtil.decrypt(data.response!!.data, privateKey)
        logger.info { decrypted }
    }

    suspend fun create(user: User, publicKey: PublicKey) = withContext(Dispatchers.IO) {
        val token: String?

        try {
            token = getToken()
        } catch (ex: Exception) {
            logger.error { ex.message }
            return@withContext;
        }

        val auth = "Bearer $token"

        val request =
            RegisterUserRequest(user)
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
}
