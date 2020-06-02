package com.ta.dodo.ui.register

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ta.dodo.repository.WalletRepositories
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RecoverViewModel : ViewModel() {
    val privateKey = MutableLiveData("")
    val recoverState = MutableLiveData(RecoverState.IDLE)

    private val walletRepositories = WalletRepositories()

    fun checkAccount() = viewModelScope.launch(Dispatchers.Main) {
        recoverState.value = RecoverState.FINDING_ACCOUNT

        val seed = privateKey.value ?: ""
        if (walletRepositories.isAccountExist(seed)) {
            recoverState.value = RecoverState.ACCOUNT_FOUND
        } else {
            recoverState.value = RecoverState.ACCOUNT_NOT_FOUND
        }
    }

    fun register() {
        recoverState.value = RecoverState.REGISTER
    }

    fun mergeAccount() {
        recoverState.value = RecoverState.MERGE
    }
}

class RecoverState {
    companion object {
        const val IDLE = 0
        const val FINDING_ACCOUNT = 1
        const val ACCOUNT_FOUND = 2
        const val ACCOUNT_NOT_FOUND = 3
        const val MERGE = 4
        const val REGISTER = 5
    }
}
