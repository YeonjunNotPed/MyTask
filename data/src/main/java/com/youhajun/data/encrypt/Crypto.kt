package com.youhajun.data.encrypt

interface Crypto {
    fun encrypt(text: String): String
    fun decrypt(encryptedData: String): String
}