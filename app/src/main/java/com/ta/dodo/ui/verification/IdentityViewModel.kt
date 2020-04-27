package com.ta.dodo.ui.verification

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    private val userRepositories = UserRepositories()

    fun submitIdentity() = viewModelScope.launch(Dispatchers.Main) {
        val pair = wallet.getKeyPair()

        logger.info { "Using ${wallet.username} wallet" }

        val user = User(wallet.username, wallet)
        user.data = User.DataBuilder()
            .addPhoneNumber(phoneNumber.value!!)
            .addEmail(email.value!!)
            .addFullName(fullName.value!!)
            .build()

        userRepositories.updateUserData(user, pair.second)
    }
}
