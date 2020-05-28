package com.ta.dodo.model.wallet

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Payment (
    val publicKey: String,
    val name: String,
    val orderId: Int,
    val total: Int,
    val webhook: String
) : Parcelable