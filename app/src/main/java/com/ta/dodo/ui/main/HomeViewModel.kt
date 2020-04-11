package com.ta.dodo.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ta.dodo.model.wallet.Wallet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.*

class HomeViewModel : ViewModel() {
    private val wallet = Wallet.getInstance()

    val balance = MutableLiveData("")
    val isRefreshingWallet = MutableLiveData(false)

    fun refreshBalance() {
        GlobalScope.launch (Dispatchers.Main) {
            isRefreshingWallet.value = true
            val localeID = Locale("in", "ID")
            val numberFormat = NumberFormat.getCurrencyInstance(localeID)
            balance.value = numberFormat.format((wallet.getBalance().toDouble() * 1000))
            isRefreshingWallet.value = false
        }
    }
}
