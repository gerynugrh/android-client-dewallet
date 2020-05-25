package com.ta.dodo.repository

import com.ta.dodo.model.wallet.TransactionHistory
import com.ta.dodo.service.RetrofitClient
import com.ta.dodo.service.wallet.CreateWallet
import com.ta.dodo.service.wallet.WalletService
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
    private val issuer = "GB6G4ZHMKS2W7QMR4BDCUXDTSMKRVQ42LIKCZTOWGFVUZUZFOVDVWB4K"
    private val asset = "IDR"
    private val server = Server("http://34.87.91.78:8000")
    private val network = Network("Standalone Network ; February 2017")
    private val httpClient = OkHttpClient()
    private val walletService: WalletService = RetrofitClient.walletService

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

    private suspend fun trustIssuer(receiverKey: KeyPair, issuerPublicKey: String, assetCode: String) = withContext(Dispatchers.IO) {
        val receiver = server.accounts().account(receiverKey.accountId)

        val asset = Asset.createNonNativeAsset(assetCode, issuerPublicKey)
        val allowAssetTransaction = Transaction.Builder(receiver, network)
            .addOperation(ChangeTrustOperation.Builder(asset, "10000000").build())
            .setTimeout(30)
            .build()
        allowAssetTransaction.sign(receiverKey)
        server.submitTransaction(allowAssetTransaction)

        return@withContext
    }

    suspend fun createWallet(seed: String) = withContext(Dispatchers.IO) {
        val wallet = KeyPair.fromSecretSeed(seed)
        val response = walletService.create(CreateWallet.Request(wallet.accountId))
        logger.info { "Response $response" }
        if (response.success) {
            trustIssuer(wallet, issuer, asset)
            logger.info { "Success authorizing IDR asset" }
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
                logger.info { record.id }
                if (record !is PaymentOperationResponse) {
                    continue
                }
                val transaction = TransactionHistory(
                    from = record.from,
                    to = record.to,
                    amount = record.amount,
                    date = record.createdAt
                )
                transactions.add(transaction)
            }
            count += 1
            operation = operation.getNextPage(httpClient)
        }

        return@withContext transactions
    }
}
