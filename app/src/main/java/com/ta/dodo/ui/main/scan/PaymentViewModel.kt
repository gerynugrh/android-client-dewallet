package com.ta.dodo.ui.main.scan

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ta.dodo.model.wallet.Payment
import com.ta.dodo.model.wallet.Wallet
import com.ta.dodo.repository.PaymentRepositories
import com.ta.dodo.repository.WalletRepositories
import com.ta.dodo.util.NumberUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import mu.KotlinLogging
import org.stellar.sdk.MemoText

private val logger = KotlinLogging.logger {}

class PaymentViewModel : ViewModel() {
    val username = MutableLiveData("")
    val amount = MutableLiveData("")

    private val walletRepositories = WalletRepositories()
    private val paymentRepositories = PaymentRepositories()
    val wallet = Wallet.getInstance()

    val paymentState = MutableLiveData(PaymentState.IDLE)

    private lateinit var payment: Payment

    fun initializePayment(payment: Payment) {
        this.payment = payment
        username.value = payment.name
        amount.value = NumberUtil.getCurrencyRepresentation(payment.total)
    }

    fun pay() = viewModelScope.launch(Dispatchers.Main) {
        logger.info { payment }
        val payment = payment
        paymentState.value = PaymentState.START
        try {
            val transactionHash = walletRepositories.sendMoney(
                seed = wallet.getSeed(),
                receiver = payment.publicKey,
                amount = payment.total.toString(),
                memo = MemoText("Order no: " + payment.orderId)
            )
            paymentRepositories.confirmPayment(
                url = payment.webhook,
                orderId = payment.orderId,
                transaction = transactionHash!!
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
