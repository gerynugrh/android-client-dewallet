package com.ta.dodo.ui.main.privacy

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ta.dodo.model.user.User
import com.ta.dodo.model.wallet.Wallet
import com.ta.dodo.repository.UserRepositories
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PrivacyViewModel(application: Application) : AndroidViewModel(application) {
    private val context = getApplication<Application>().applicationContext
    private val wallet = Wallet.getInstance()

    private val userRepositories = UserRepositories()
    val authorizeUserState = MutableLiveData(AuthorizeUserState.IDLE)
    val greeting = MutableLiveData("Halo ${wallet.username},")

    val query = MutableLiveData("")

    fun authorize() = viewModelScope.launch(Dispatchers.Main) {
        authorizeUserState.value = AuthorizeUserState.START
        val pair = wallet.getEncryptionKeyPair()
        val signingPair = wallet.getSigningKeyPair()

        val username = query.value!!
        val user = User(wallet.username, wallet.getAccountId(), pair.second)
        userRepositories.addKey(user, username, user.getSecretKey(), signingPair.first)

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
