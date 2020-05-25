package com.ta.dodo.repository

import com.google.gson.Gson
import com.ta.dodo.model.user.CipherUtil
import com.ta.dodo.model.user.User
import com.ta.dodo.service.RetrofitClient
import com.ta.dodo.service.user.UserService
import com.ta.dodo.service.user.request.*
import com.ta.dodo.service.user.response.BaseResponse
import com.ta.dodo.service.user.response.GetUserDataResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mu.KotlinLogging
import java.security.Key
import java.security.PrivateKey
import javax.crypto.SecretKey

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

    suspend fun updateUserData(user: User, key: SecretKey) = withContext(Dispatchers.IO) {
        val token = getToken()
        val auth = "Bearer $token"

        logger.info { auth }
        val dataJson = gson.toJson(user.data)
        val encryptedData = CipherUtil.encryptWithoutProvider(dataJson, key, CipherUtil.AES)
        val request = UpdateUserDataRequest(user.username, encryptedData)

        userService.updateUserData(request, auth)
        addKey(user, user.username, key)
    }

    suspend fun addKey(user: User, owner: String, key: Key) = withContext(Dispatchers.IO) {
        val token = getToken()
        val auth = "Bearer $token"

        logger.info { auth }
        val ePublicKeyString = getEncryptionPublicKey(owner)
        val ePublicKey = CipherUtil.decodePublicKey(ePublicKeyString)
        logger.info { "Succesfully decoded public key" }

        try {
            val keyString = CipherUtil.encode(key.encoded)
            logger.info { "Encode secret key $keyString" }

            val encryptedKey = CipherUtil.encryptWithoutProvider(keyString, ePublicKey, CipherUtil.RSA)
            logger.info { "Succesfully encrypting symetric key" }

            val request = InsertKeyRequest(user.username, owner, encryptedKey)
            userService.addKey(request, auth)
        } catch (ex: Exception) {
            logger.error { ex.printStackTrace() }
        }
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

        val decrypted = CipherUtil.decryptWithoutProvider(data.data!!.data, privateKey, CipherUtil.RSA)
        logger.info { decrypted }
    }

    suspend fun getUserData(username: String, owner: String, privateKey: PrivateKey) = withContext(Dispatchers.IO) {
        val token = getToken()
        val auth = "Bearer $token"

        val request = GetUserDataRequest(username, owner)
        val response: BaseResponse<GetUserDataResponse>

        response = userService.getUserData(request, auth)
        val ePublicKeyString = response.data!!.ePublicKey
        val ePublicKey = CipherUtil.decodePublicKey(ePublicKeyString)
        val encryptedKey = response.data!!.key
        val encryptedData = response.data!!.data

        val user = User(
            username = username,
            publicKey = response.data!!.publicKey,
            ePublicKey = ePublicKey
        )
        if (encryptedData == "" || encryptedKey == "") {
            return@withContext user
        }

        try {
            val decryptedKeyText = CipherUtil.decryptWithoutProvider(encryptedKey, privateKey, CipherUtil.RSA)
            val decodedKeyText = CipherUtil.decode(decryptedKeyText)
            val decryptedKey = CipherUtil.decodeSecretKey(decodedKeyText)

            logger.info { "Decrypted key ${CipherUtil.encode(decryptedKey.encoded)}" }

            logger.info { "Data $encryptedData" }
            val decryptedData = CipherUtil.decryptWithoutProvider(encryptedData, decryptedKey, CipherUtil.AES)
            val data = gson.fromJson(decryptedData, User.Data::class.java)

            user.data = data

            return@withContext user
        } catch (ex: Exception) {
            logger.error { ex.printStackTrace() }
            return@withContext user
        }
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

    suspend fun getEncryptionPublicKey(username: String) = withContext(Dispatchers.IO) {
        val token = getToken()
        val auth = "Bearer $token"

        logger.info { "Querying $username" }

        val request = GetPublicKeyRequest(username)
        try {
            val data = userService.getPublicKey(request, auth)
            return@withContext data.data!!.ePublicKey
        } catch (ex: Exception) {
            throw UsernameNotFoundException(username)
        }
    }

    suspend fun create(user: User) = withContext(Dispatchers.IO) {
        val token = getToken()
        val auth = "Bearer $token"

        val username = user.username
        val publicKey = user.publicKey
        val ePublicKeyText = CipherUtil.encode(user.ePublicKey.encoded)

        val request = RegisterUserRequest(username, publicKey, ePublicKeyText)
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
