package com.ta.dodo.ui.main

import android.content.Intent
import android.view.View
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ta.dodo.VerificationActivity
import com.ta.dodo.model.wallet.TransactionHistory
import com.ta.dodo.model.wallet.Wallet
import com.ta.dodo.repository.UserRepositories
import com.ta.dodo.repository.WalletRepositories
import com.ta.dodo.ui.main.adapter.TransactionsHistoryAdapter
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
    private val walletRepositories = WalletRepositories()

    val balance = MutableLiveData("")
    val isRefreshingWallet = MutableLiveData(false)
    val isDataInitialized = MutableLiveData(true)
    val isClickingVerification = MutableLiveData(false)

    private var transactions = ArrayList<TransactionHistory>()
    var transactionsHistoryAdapter = TransactionsHistoryAdapter(transactions, wallet.getAccountId())

    val historyTransactionState = MutableLiveData(HistoryTransactionState.IDLE)

    init {
        getUserData()
    }

    private fun getUserData() = viewModelScope.launch(Dispatchers.Main) {
        try {
            val pair = wallet.getKeyPair()
            userRepositories.getOwnData(wallet.username, pair.first)
        } catch (ex: UserRepositories.DataNotInitializedException) {
            isDataInitialized.value = false
        }
    }

    private fun refreshTransactions() = viewModelScope.launch(Dispatchers.Main) {
        val accountId = wallet.getAccountId()
        historyTransactionState.value = HistoryTransactionState.START
        try {
            transactions = walletRepositories.getTransactions(accountId)
            transactionsHistoryAdapter.addAll(transactions)

            historyTransactionState.value = HistoryTransactionState.FINISHED
        } catch (ex: Exception) {
            historyTransactionState.value = HistoryTransactionState.ERROR
        }
    }

    private fun refreshBalance() = viewModelScope.launch(Dispatchers.Main) {
        isRefreshingWallet.value = true
        val localeID = Locale("in", "ID")
        val numberFormat = NumberFormat.getCurrencyInstance(localeID)

        val balance: Double?

        try {
            balance = wallet.getBalance().toDouble() * 100
            this@HomeViewModel.balance.value = numberFormat.format((balance))
        } catch (ex: Exception) {
            logger.error { ex.message }
        }

        isRefreshingWallet.value = false
    }

    fun refreshScreen() = viewModelScope.launch(Dispatchers.Main) {
        refreshBalance()
        refreshTransactions()
    }

    fun navigateToVerificationActivity() {
        isClickingVerification.value = true
    }
}

class HistoryTransactionState {
    companion object {
        const val IDLE = 0
        const val START = 1
        const val FINISHED = 2
        const val ERROR = 3
    }
}
