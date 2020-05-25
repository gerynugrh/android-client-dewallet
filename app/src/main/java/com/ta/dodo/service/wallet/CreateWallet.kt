package com.ta.dodo.service.wallet

class CreateWallet {
    data class Request(val publicKey: String)
    data class Response(val success: Boolean)
}