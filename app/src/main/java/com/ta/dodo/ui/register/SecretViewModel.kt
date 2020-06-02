package com.ta.dodo.ui.register

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SecretViewModel : ViewModel() {
    val secret = MutableLiveData("")
    val viewModelState = MutableLiveData(State.IDLE)

    fun finishSavingSecret() {
        viewModelState.value = State.FINISH
    }

    class State {
        companion object {
            const val IDLE = 0
            const val FINISH = 1
        }
    }
}
