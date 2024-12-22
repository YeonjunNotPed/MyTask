package com.youhajun.data.encrypt

import android.security.keystore.KeyProperties
import android.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.GCMParameterSpec
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EncryptUtil @Inject constructor(
    private val keyStoreUtil:KeyStoreUtil
): Crypto {

    override fun decrypt(encryptedData: String): String {
        val secretKey = keyStoreUtil.getKeyOrGenerate()

        val cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/" + KeyProperties.BLOCK_MODE_GCM + "/" + KeyProperties.ENCRYPTION_PADDING_NONE)
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)

        val combined = Base64.decode(encryptedData, Base64.DEFAULT)
        val iv = combined.sliceArray(0 until 12)
        val ciphertext = combined.sliceArray(12 until combined.size)

        cipher.init(Cipher.DECRYPT_MODE, secretKey, GCMParameterSpec(128, iv))
        val plaintext = cipher.doFinal(ciphertext)
        return String(plaintext, Charsets.UTF_8)
    }

    override fun encrypt(text: String): String {
        val secretKey = keyStoreUtil.getKeyOrGenerate()

        val cipher =
            Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/" + KeyProperties.BLOCK_MODE_GCM + "/" + KeyProperties.ENCRYPTION_PADDING_NONE)
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)
        val iv = cipher.iv

        val ciphertext = cipher.doFinal(text.toByteArray(Charsets.UTF_8))
        val combined = iv + ciphertext
        return Base64.encodeToString(combined, Base64.DEFAULT)
    }
}