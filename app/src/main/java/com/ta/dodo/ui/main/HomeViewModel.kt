package com.ta.dodo.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
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

    init {
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
}
