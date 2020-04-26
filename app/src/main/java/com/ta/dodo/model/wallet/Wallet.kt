package com.ta.dodo.model.wallet

import android.content.Context
import com.ta.dodo.model.user.KeyGenerator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mu.KotlinLogging
import org.stellar.sdk.KeyPair
import org.stellar.sdk.Server
import java.lang.Exception
import java.nio.charset.StandardCharsets
import java.security.PrivateKey
import java.security.PublicKey

private val logger = KotlinLogging.logger {}

class Wallet(val username: String) {
    private lateinit var keyPair: KeyPair

    companion object {
        private val wallets = HashMap<String, Wallet>()
        private const val privateKeyFilename = "uangsaku.pk"
        private lateinit var instance: Wallet

        fun load(context: Context): Wallet {
            val reader = context.openFileInput(privateKeyFilename).bufferedReader()
            val username = reader.readLine()
            val secretSeed = reader.readLine()

            val keyPair = KeyPair.fromSecretSeed(secretSeed)

            logger.info { "Seed: ${String(keyPair.secretSeed)}" }
            logger.info { "AccountID: ${keyPair.accountId}" }

            return Wallet(username, keyPair)
        }

        fun setInstance(wallet: Wallet) {
            instance = wallet
        }

        fun getInstance(): Wallet {
            return instance
        }
    }

    constructor(username: String, keyPair: KeyPair): this(username) {
        this.keyPair = keyPair
        wallets[username] = this
    }

    suspend fun register() = withContext(Dispatchers.Default) {
        keyPair = KeyPair.random()

        logger.info { "Seed: ${String(keyPair.secretSeed)}" }
        logger.info { "AccountID: ${keyPair.accountId}" }
        // TODO: Register the public key to Hyperledger
    }

    suspend fun save(context: Context) = withContext(Dispatchers.IO) {
        if (!::keyPair.isInitialized) {
            throw WalletNotRegisteredException(username)
        }
        context.openFileOutput(privateKeyFilename, Context.MODE_PRIVATE).use {
            it.write(username.toByteArray())
            it.write("\n".toByteArray())
            it.write(String(keyPair.secretSeed).toByteArray())
        }
    }

    suspend fun getBalance(): String = withContext(Dispatchers.IO) {
        val server = Server("http://134.209.97.218:8000")
        val account = server.accounts().account(keyPair.accountId)

        logger.info { "Balance ${account.balances[0].balance}" }

        return@withContext account.balances[0].balance
    }

    suspend fun generateKeyPair() {
        val alias = getAccountId()
        val seed = getSeed().toByteArray(StandardCharsets.UTF_8)

        KeyGenerator.build(alias, seed)
    }

    suspend fun getKeyPair(): Pair<PrivateKey, PublicKey> {
        val alias = getAccountId()
        val seed = getSeed().toByteArray(StandardCharsets.UTF_8)

        val keyGenerator = KeyGenerator.build(alias, seed)
        return Pair(keyGenerator.getPrivateKey()!!, keyGenerator.getPublicKey()!!)
    }

    fun getAccountId(): String {
        return keyPair.accountId
    }

    private fun getSeed(): String {
        return String(keyPair.secretSeed)
    }
}

class WalletNotRegisteredException(username: String?) :
    Exception("Wallet from $username not registered")
