package com.ta.dodo

fun ByteArray.toHexString() = joinToString("") { "%02x".format(it) }
