package com.ta.dodo.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mu.KotlinLogging
import org.stellar.sdk.*


private val logger = KotlinLogging.logger {}

class WalletRepositories {
    private val server = Server("http://34.87.91.78:8000")
    private val network = Network("Standalone Network ; February 2017")

    suspend fun sendMoney(seed: String, receiver: String, amount: String) = withContext(Dispatchers.IO) {
        try {
            server.accounts().account(receiver)
        } catch (ex: Exception) {

        }

        val source = KeyPair.fromSecretSeed(seed)
        val sourceAccount = server.accounts().account(seed)

        val operation = PaymentOperation.Builder(receiver, AssetTypeNative(), amount).build()
        val transaction = Transaction.Builder(sourceAccount, network)
            .addOperation(operation)
            .setTimeout(180)
            .build()

        transaction.sign(source)
        try {
            server.submitTransaction(transaction)
        } catch (ex: Exception) {

        }
    }
}
