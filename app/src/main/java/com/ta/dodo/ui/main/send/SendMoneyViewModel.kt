package com.ta.dodo.ui.main.send

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ta.dodo.repository.UserRepositories
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import mu.KotlinLogging

private val logger = KotlinLogging.logger {}

class SendMoneyViewModel : ViewModel() {
    private val userRepositories = UserRepositories()
    val query = MutableLiveData("")

    var username: String = ""
    var publicKey: String = ""

    val isPublicKeyFetched = MutableLiveData(false)

    fun sendMoney() = viewModelScope.launch(Dispatchers.Main) {
        username = query.value!!
        publicKey = userRepositories.getPublicKey(username)

        logger.info { publicKey }

        isPublicKeyFetched.value = true
    }
}
