package com.ta.dodo.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import mu.KotlinLogging
import kotlinx.coroutines.*
import org.stellar.sdk.KeyPair

class RegisterViewModel : ViewModel() {
    private val logger = KotlinLogging.logger {}

    var keyPair: MutableLiveData<KeyPair> = MutableLiveData()
    var isGeneratingKey: MutableLiveData<Boolean> = MutableLiveData(false)

    fun register() {
        GlobalScope.launch (Dispatchers.Main) {
            isGeneratingKey.value = true
            val keyPair = KeyPair.random()

            logger.info { "Public key ${keyPair.accountId}" }
            logger.info { "Seed ${keyPair.secretSeed}" }

            this@RegisterViewModel.keyPair.value = keyPair
            isGeneratingKey.value = false
        }
    }
}
