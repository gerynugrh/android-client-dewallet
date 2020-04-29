package com.ta.dodo.ui.main.send

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.davidmiguel.numberkeyboard.NumberKeyboardListener
import com.ta.dodo.model.wallet.Wallet
import com.ta.dodo.util.NumberUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import mu.KotlinLogging

private val logger = KotlinLogging.logger {}

class SetAmountViewModel : ViewModel(), NumberKeyboardListener {

    private val wallet = Wallet.getInstance()

    val amount = MutableLiveData("")
    val username = MutableLiveData("")
    val publicKey = MutableLiveData("")

    val transactionState = MutableLiveData(Transaction.IDLE)

    private var mAmount: Int = 0

    fun confirmTransaction() = viewModelScope.launch(Dispatchers.Main) {
        transactionState.value = Transaction.START

        val publicKey = publicKey.value!!
        try {
            wallet.sendMoney(publicKey, mAmount.toString())
        } catch (ex: Exception) {
            logger.error { ex.message }
        }

        transactionState.value = Transaction.FINISHED
    }

    override fun onLeftAuxButtonClicked() {
        TODO("Not yet implemented")
    }

    override fun onNumberClicked(number: Int) {
        mAmount *= 10
        mAmount += number

        amount.value = NumberUtil.getCurrencyRepresentation(mAmount)
    }

    override fun onRightAuxButtonClicked() {
        mAmount /= 10
        if (mAmount == 0) {
            amount.value = ""
            return
        }
        amount.value = NumberUtil.getCurrencyRepresentation(mAmount)
    }
}

class Transaction {
    companion object {
        const val IDLE = 0
        const val START = 1
        const val FINISHED = 2
    }
}
