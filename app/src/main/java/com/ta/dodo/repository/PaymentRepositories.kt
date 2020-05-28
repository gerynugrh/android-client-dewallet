package com.ta.dodo.repository

import com.ta.dodo.service.payment.PaymentService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mu.KotlinLogging
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private val logger = KotlinLogging.logger {  }

class PaymentRepositories {
    suspend fun confirmPayment(url: String, orderId: Int, transaction: String) = withContext(Dispatchers.IO) {
        val service = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(url)
            .build()

        val client = service.create(PaymentService::class.java)
        val response = client.confirm(orderId, transaction)

        logger.info { response }

        return@withContext response
    }
}
