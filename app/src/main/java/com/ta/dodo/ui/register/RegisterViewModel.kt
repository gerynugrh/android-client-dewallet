package com.ta.dodo.ui.register

import android.app.Application
import android.util.Base64.encodeToString
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ta.dodo.model.user.CipherUtil
import com.ta.dodo.model.user.User
import com.ta.dodo.model.wallet.Wallet
import com.ta.dodo.repository.UserRepositories
import kotlinx.coroutines.*
import mu.KotlinLogging
import java.util.*

private val logger = KotlinLogging.logger {}

class RegisterViewModel(application: Application) : AndroidViewModel(application) {
    private val context = getApplication<Application>().applicationContext
    private val userRepositories = UserRepositories()

    var wallet: MutableLiveData<Wallet> = MutableLiveData()
    var username: MutableLiveData<String> = MutableLiveData("")
    var isGeneratingKey: MutableLiveData<Boolean> = MutableLiveData(false)
    var isRegisterStepComplete: MutableLiveData<Boolean> = MutableLiveData(false)

    fun loadSavedPrivateKey() {
        val wallet = Wallet.load(context)
        Wallet.setInstance(wallet)
    }

    fun register() = viewModelScope.launch(Dispatchers.Main) {
        isGeneratingKey.value = true
        val username = this@RegisterViewModel.username.value!!
        val wallet = Wallet(username)

        wallet.register()
        wallet.save(context)

        logger.info { "Finished generating key" }

        wallet.generateKeyPair()
        val publicKey = wallet.getAccountId()

        val user = User(wallet.username, publicKey, wallet.getKeyPair().second)

        userRepositories.create(user)
        Wallet.setInstance(wallet)

        this@RegisterViewModel.wallet.value = wallet
        isGeneratingKey.value = false
        isRegisterStepComplete.value = true
    }
}
