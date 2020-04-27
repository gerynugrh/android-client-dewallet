package com.ta.dodo.ui.main

import android.app.Application
import android.content.Intent
import android.view.View
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ta.dodo.VerificationActivity
import com.ta.dodo.model.wallet.Wallet
import com.ta.dodo.repository.UserRepositories
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import mu.KotlinLogging
import java.text.NumberFormat
import java.util.*

private val logger = KotlinLogging.logger {}

class HomeViewModel : ViewModel() {
    private val wallet = Wallet.getInstance()
    private val userRepositories = UserRepositories()

    val balance = MutableLiveData("")
    val isRefreshingWallet = MutableLiveData(false)
    val isDataInitialized = MutableLiveData(true)

    init {
        getUserData()
    }

    private fun getUserData() = viewModelScope.launch(Dispatchers.Main) {
        try {
            val pair = wallet.getKeyPair()
            userRepositories.getData(wallet.username, pair.first)
        } catch (ex: UserRepositories.DataNotInitializedException) {
            isDataInitialized.value = false
        }
    }

    fun refreshBalance() {
        GlobalScope.launch (Dispatchers.Main) {
            isRefreshingWallet.value = true
            val localeID = Locale("in", "ID")
            val numberFormat = NumberFormat.getCurrencyInstance(localeID)

            var balance: Double? = null

            try {
                balance = wallet.getBalance().toDouble() * 1000
            } catch (ex: Exception) {
                logger.error { ex.message }
            }

            this@HomeViewModel.balance.value = numberFormat.format((balance))
            isRefreshingWallet.value = false
        }
    }

    fun navigateToVerificationActivity(view: View) {
        val context = view.context
        val intent = Intent(context, VerificationActivity::class.java)
        startActivity(context, intent, null)
    }
}
