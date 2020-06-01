package com.ta.dodo.ui.verification

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ta.dodo.model.user.KeyUtil
import com.ta.dodo.model.user.User
import com.ta.dodo.model.wallet.Wallet
import com.ta.dodo.repository.UserRepositories
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import mu.KotlinLogging

private val logger = KotlinLogging.logger {}

class IdentityViewModel : ViewModel() {

    private val wallet = Wallet.getInstance()

    val fullName = MutableLiveData<String>()
    val email = MutableLiveData<String>()
    val phoneNumber = MutableLiveData<String>()

    val isGoingBackToHome = MutableLiveData(false)

    private val userRepositories = UserRepositories()

    fun submitIdentity() = viewModelScope.launch(Dispatchers.Main) {
        try {
            logger.info { "Check 1" }
            val pair = wallet.getEncryptionKeyPair()
            val signingPair = wallet.getSigningKeyPair()

            logger.info { "Using ${wallet.username} wallet" }
            val publicKey = wallet.getAccountId()

            val keyUtil = KeyUtil.instance
            val key = keyUtil.secretKey

            val user = User(wallet.username, publicKey, pair.second)
            user.data = User.DataBuilder()
                .addPhoneNumber(phoneNumber.value!!)
                .addEmail(email.value!!)
                .addFullName(fullName.value!!)
                .build()

            logger.info { "Check 2" }

            userRepositories.updateUserData(user, key, signingPair.first)
            isGoingBackToHome.value = true
        } catch (ex: Exception) {
            logger.error { ex.printStackTrace() }
        }
    }
}
