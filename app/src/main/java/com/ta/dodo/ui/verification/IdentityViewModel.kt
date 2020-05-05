package com.ta.dodo.ui.verification

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ta.dodo.model.user.CipherUtil
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
        logger.info { "Check 1" }
        val pair = wallet.getKeyPair()

        logger.info { "Using ${wallet.username} wallet" }
        val ePublicKey = CipherUtil.encode(pair.second.encoded)!!
        val publicKey = wallet.getAccountId()

        val user = User(wallet.username, publicKey, ePublicKey)
        user.data = User.DataBuilder()
            .addPhoneNumber(phoneNumber.value!!)
            .addEmail(email.value!!)
            .addFullName(fullName.value!!)
            .build()

        logger.info { "Check 2" }

        userRepositories.updateUserData(user, pair.second)
        isGoingBackToHome.value = true
    }
}
