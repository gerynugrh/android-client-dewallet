package com.ta.dodo.repository

import com.ta.dodo.model.wallet.TransactionHistory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mu.KotlinLogging
import org.stellar.sdk.*
import org.stellar.sdk.requests.ErrorResponse
import org.stellar.sdk.responses.AccountResponse
import org.stellar.sdk.responses.SubmitTransactionUnknownResponseException
import org.stellar.sdk.responses.operations.PaymentOperationResponse
import shadow.okhttp3.OkHttpClient


private val logger = KotlinLogging.logger {}

class WalletRepositories {
    private val server = Server("http://34.87.91.78:8000")
    private val network = Network("Standalone Network ; February 2017")
    private val httpClient = OkHttpClient()

    suspend fun sendMoney(seed: String, receiver: String, amount: String) = withContext(Dispatchers.IO) {
        try {
            server.accounts().account(receiver)
        } catch (ex: Exception) {

        }

        logger.info { "Initiating transaction from $seed amount $amount" }

        val source = KeyPair.fromSecretSeed(seed)
        lateinit var sourceAccount: AccountResponse
        try {
            sourceAccount = server.accounts().account(source.accountId)
        } catch (ex: ErrorResponse) {
            logger.error { ex.body }
        }

        logger.info { "Success building sender account" }

        val operation = PaymentOperation.Builder(receiver, AssetTypeNative(), amount).build()
        val transaction = Transaction.Builder(sourceAccount, network)
            .addOperation(operation)
            .setTimeout(180)
            .build()

        logger.info { "Success buildling transaction" }

        transaction.sign(source)
        try {
            server.submitTransaction(transaction)
        } catch (ex: SubmitTransactionUnknownResponseException) {
                logger.error { "Code ${ex.code} Message: ${ex.body}" }
        }
    }

    suspend fun getTransactions(accountId: String) = withContext(Dispatchers.IO) {
        val operationBuilder = server.operations().forAccount(accountId)
        val operations = operationBuilder.execute()

        val transactions = ArrayList<TransactionHistory>()
        var operation = operations
        var count = 0
        while (operation != null && count < 5) {
            for (record in operation.records) {
                if (record !is PaymentOperationResponse) {
                    break
                }
                val transaction = TransactionHistory(
                    from = record.from,
                    to = record.to,
                    amount = record.amount
                )
                transactions.add(transaction)
            }
            count += 1
            operation = operation.getNextPage(httpClient)
        }

        return@withContext transactions
    }
}
