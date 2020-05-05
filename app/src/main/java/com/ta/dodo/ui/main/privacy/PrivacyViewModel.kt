package com.ta.dodo.ui.main.privacy

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ta.dodo.model.user.User
import com.ta.dodo.model.wallet.Wallet
import com.ta.dodo.repository.UserRepositories
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PrivacyViewModel : ViewModel() {
    private val wallet = Wallet.getInstance()

    private val userRepositories = UserRepositories()
    val authorizeUserState = MutableLiveData(AuthorizeUserState.IDLE)

    val query = MutableLiveData("")

    fun authorize() = viewModelScope.launch(Dispatchers.Main) {
        authorizeUserState.value = AuthorizeUserState.START

        val username = query.value!!
        val user = User(wallet.username, wallet.getAccountId(), wallet.getKeyPair().second)

        user.generateSecretKey()
        userRepositories.addKey(user, username, user.getSecretKey())

        authorizeUserState.value = AuthorizeUserState.FINISHED
    }
}

class AuthorizeUserState {
    companion object {
        const val IDLE = 0
        const val START = 1
        const val FINISHED = 2
    }
}
