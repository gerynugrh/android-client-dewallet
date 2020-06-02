package com.ta.dodo.ui.register

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ta.dodo.model.user.KeyUtil
import com.ta.dodo.model.user.User
import com.ta.dodo.model.wallet.Wallet
import com.ta.dodo.repository.UserRepositories
import com.ta.dodo.repository.WalletRepositories
import kotlinx.coroutines.*
import mu.KotlinLogging

private val logger = KotlinLogging.logger {}

class RegisterViewModel(application: Application) : AndroidViewModel(application) {
    private val context = getApplication<Application>().applicationContext
    private val userRepositories = UserRepositories()
    private val walletRepositories = WalletRepositories()

    var wallet: MutableLiveData<Wallet> = MutableLiveData()
    var secret = MutableLiveData("")
    var username = MutableLiveData("")
    var registerState = MutableLiveData(RegisterState.IDLE)

    fun loadSavedPrivateKey() {
        val wallet = Wallet.load(context)
        viewModelScope.launch(Dispatchers.Main) {
            val keyUtil = KeyUtil.build(wallet.username, context)
            KeyUtil.instance = keyUtil
        }
        Wallet.setInstance(wallet)
    }

    fun register() = viewModelScope.launch(Dispatchers.Main) {
        registerState.value = RegisterState.GENERATING
        val username = this@RegisterViewModel.username.value!!
        val wallet = Wallet(username)

        wallet.register()
        wallet.save(context)

        logger.info { "Finished generating key" }

        wallet.generateKeyPair()
        val publicKey = wallet.getAccountId()
        val encryptionKey = wallet.getEncryptionKeyPair()
        val signingKey = wallet.getSigningKeyPair()

        val user = User(wallet.username, publicKey, encryptionKey.second, signingKey.second)
        user.generateSecretKey(context)

        userRepositories.create(user)
        walletRepositories.createWallet(wallet.getSeed())
        Wallet.setInstance(wallet)

        this@RegisterViewModel.wallet.value = wallet
        secret.value = wallet.getSeed()
        registerState.value = RegisterState.GENERATED
    }

    fun finishWritingSecret() {
        registerState.value = RegisterState.FINISH
    }

    fun recoverAccount() {
        registerState.value = RegisterState.RECOVER
    }
}

class RegisterState {
    companion object {
        const val IDLE = 0
        const val GENERATING = 1
        const val GENERATED = 2
        const val FINISH = 3
        const val RECOVER = 4
    }
}
