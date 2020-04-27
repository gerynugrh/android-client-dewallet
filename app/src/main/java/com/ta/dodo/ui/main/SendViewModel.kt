package com.ta.dodo.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ta.dodo.repository.UserRepositories
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SendViewModel : ViewModel() {
    private val userRepositories = UserRepositories()
    val query = MutableLiveData("")

    fun sendMoney() = viewModelScope.launch(Dispatchers.Main) {
        val username = query.value!!
        val receiver = userRepositories.getPublicKey(username)
    }
}
