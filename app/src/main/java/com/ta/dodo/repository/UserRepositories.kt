package com.ta.dodo.repository

import com.google.gson.Gson
import com.ta.dodo.model.user.CipherUtil
import com.ta.dodo.model.user.User
import com.ta.dodo.service.RetrofitClient
import com.ta.dodo.service.user.request.RegisterUserRequest
import com.ta.dodo.service.user.UserService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mu.KotlinLogging
import okhttp3.MediaType
import okhttp3.RequestBody
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

    suspend fun create(user: User, publicKey: PublicKey) = withContext(Dispatchers.IO) {
        val dataJson = gson.toJson(user.data)

        val encrypted = CipherUtil.encrypt(
            dataJson,
            publicKey
        )

        logger.info { "Encrypted data $encrypted" }

        val token: String?

        try {
            token = getToken()
        } catch (ex: Exception) {
            logger.error { ex.message }
            return@withContext;
        }

        val authHeader = "Bearer $token"

        val request =
            RegisterUserRequest(user, encrypted)
        logger.info { authHeader }
        try {
            userService.register(request, authHeader)
        } catch (ex: Exception) {
            logger.error { ex.message }
        }
    }
}