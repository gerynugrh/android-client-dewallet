package com.ta.dodo.ui.main.send

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ta.dodo.model.wallet.Wallet
import com.ta.dodo.repository.UserRepositories
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import mu.KotlinLogging

private val logger = KotlinLogging.logger {}

class SendMoneyViewModel : ViewModel() {
    private val userRepositories = UserRepositories()
    val wallet = Wallet.getInstance()
    val query = MutableLiveData("")

    var username: String = ""
    var publicKey: String = ""
    var identifier: String = ""

    val searchPublicKeyState = MutableLiveData(SearchPublicKey.IDLE)

    val isPublicKeyFetched = MutableLiveData(false)

    fun sendMoney() = viewModelScope.launch(Dispatchers.Main) {
        searchPublicKeyState.value = SearchPublicKey.START

        username = query.value!!
        val user = userRepositories.getUserData(username, wallet.username)

        publicKey = user.publicKey
        identifier = user.data?.fullName ?: user.username

        searchPublicKeyState.value = SearchPublicKey.FINISHED
    }
}

class SearchPublicKey {
    companion object {
        const val IDLE = 0
        const val START = 1
        const val FINISHED = 2
    }
}
