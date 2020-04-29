package com.ta.dodo.ui.main.send

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.davidmiguel.numberkeyboard.NumberKeyboardListener
import com.ta.dodo.util.NumberUtil

class SetAmountViewModel : ViewModel(), NumberKeyboardListener {

    val amount = MutableLiveData("")
    private var mAmount: Int = 0

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
