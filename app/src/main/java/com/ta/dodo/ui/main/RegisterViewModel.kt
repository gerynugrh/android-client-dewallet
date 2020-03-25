package com.ta.dodo.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.ta.dodo.model.wallet.Wallet
import kotlinx.coroutines.*
import mu.KotlinLogging

private val logger = KotlinLogging.logger {}

class RegisterViewModel(application: Application) : AndroidViewModel(application) {
    private val context = getApplication<Application>().applicationContext

    var wallet: MutableLiveData<Wallet> = MutableLiveData()
    var username: MutableLiveData<String> = MutableLiveData("")
    var isGeneratingKey: MutableLiveData<Boolean> = MutableLiveData(false)

    fun loadSavedPrivateKey() {
        val wallet = Wallet.load(context)
    }

    fun register() {
        GlobalScope.launch (Dispatchers.Main) {
            isGeneratingKey.value = true
            val wallet = Wallet(username.value ?: "")

            wallet.register()
            wallet.save(context)

            logger.info { "Finished generating key" }

            this@RegisterViewModel.wallet.value = wallet
            isGeneratingKey.value = false
        }
    }
}
