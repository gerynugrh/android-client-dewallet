package com.ta.dodo.ui.main.scan

import android.graphics.PointF
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dlazaro66.qrcodereaderview.QRCodeReaderView
import com.google.gson.Gson
import com.ta.dodo.model.wallet.Payment
import com.ta.dodo.model.wallet.Wallet
import com.ta.dodo.repository.WalletRepositories
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import mu.KotlinLogging

private val logger = KotlinLogging.logger{}

class ScanViewModel : ViewModel(), QRCodeReaderView.OnQRCodeReadListener {
    val payment: MutableLiveData<Payment> = MutableLiveData()

    override fun onQRCodeRead(text: String?, points: Array<out PointF>?) {
        val gson = Gson()
        try {
            val data = gson.fromJson(text, Payment::class.java)
            if (data.publicKey == "") {
                throw InvalidPaymentFormatException(data)
            }
            payment.value = data
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    class InvalidPaymentFormatException(payment: Payment): Exception( "$payment is invalid")
}
