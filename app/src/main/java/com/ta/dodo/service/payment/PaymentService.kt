package com.ta.dodo.service.payment

import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface PaymentService {
    @FormUrlEncoded
    @POST("?wc-api=wc_gateway_uangkita")
    suspend fun confirm(
        @Field("orderId") orderId: Int,
        @Field("transaction") transaction: String
    ): ConfirmPayment.Response
}
