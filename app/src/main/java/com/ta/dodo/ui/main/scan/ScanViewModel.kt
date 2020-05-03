package com.ta.dodo.ui.main.scan

import android.graphics.PointF
import androidx.lifecycle.ViewModel
import com.dlazaro66.qrcodereaderview.QRCodeReaderView
import mu.KotlinLogging

private val logger = KotlinLogging.logger{}

class ScanViewModel : ViewModel(), QRCodeReaderView.OnQRCodeReadListener {
    override fun onQRCodeRead(text: String?, points: Array<out PointF>?) {
        logger.info { text }
    }
}
