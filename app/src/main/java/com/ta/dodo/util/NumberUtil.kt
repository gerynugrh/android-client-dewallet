package com.ta.dodo.util

import java.text.DecimalFormat
import java.text.NumberFormat

class NumberUtil {
    companion object {
        fun getCurrencyRepresentation(value: Int): String {
            val numberFormat = DecimalFormat()
            return "Rp${numberFormat.format(value)}"
        }
    }
}