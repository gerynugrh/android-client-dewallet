package com.ta.dodo.ui.main.scan

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ta.dodo.model.wallet.Payment
import com.ta.dodo.model.wallet.Wallet
import com.ta.dodo.repository.WalletRepositories
import com.ta.dodo.util.NumberUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PaymentViewModel : ViewModel() {
    val username = MutableLiveData("")
    val amount = MutableLiveData("")

    private val walletRepositories = WalletRepositories()
    val wallet = Wallet.getInstance()

    val paymentState = MutableLiveData(PaymentState.IDLE)

    var payment: Payment? = null
        set(payment) {
            initializePayment(payment)
        }

    private fun initializePayment(payment: Payment?) {
        username.value = payment?.username
        amount.value = NumberUtil.getCurrencyRepresentation(payment?.price ?: 0)
    }

    fun pay() = viewModelScope.launch(Dispatchers.Main) {
        val payment = payment!!
        paymentState.value = PaymentState.START
        try {
            walletRepositories.sendMoney(
                seed = wallet.getSeed(),
                receiver = payment.publicKey,
                amount = payment.price.toString()
            )
            paymentState.value = PaymentState.SUCCESS
        } catch (ex: Exception) {
            ex.printStackTrace()
            paymentState.value = PaymentState.ERROR
        }
    }
}

class PaymentState {
    companion object {
        const val IDLE = 0
        const val START = 1
        const val SUCCESS = 2
        const val ERROR = 3
    }
}
