package com.ta.dodo.ui.register

import android.app.Application
import android.content.Intent
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ta.dodo.MainActivity
import com.ta.dodo.model.user.User
import com.ta.dodo.model.wallet.Wallet
import com.ta.dodo.repository.UserRepositories
import kotlinx.coroutines.*
import mu.KotlinLogging

private val logger = KotlinLogging.logger {}

class RegisterViewModel(application: Application) : AndroidViewModel(application) {
    private val context = getApplication<Application>().applicationContext
    private val userRepositories = UserRepositories()

    var wallet: MutableLiveData<Wallet> = MutableLiveData()
    var username: MutableLiveData<String> = MutableLiveData("")
    var isGeneratingKey: MutableLiveData<Boolean> = MutableLiveData(false)

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
        val user = User(wallet.username, wallet)

        userRepositories.create(user)

        this@RegisterViewModel.wallet.value = wallet
        isGeneratingKey.value = false
    }
}
