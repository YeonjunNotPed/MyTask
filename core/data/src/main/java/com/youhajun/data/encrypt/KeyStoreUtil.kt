package com.youhajun.data.encrypt

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.security.KeyStore
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.inject.Singleton

object KeyStoreUtil {
    private const val ANDROID_KEY_STORE = "AndroidKeyStore"
    private const val MY_TASK_KEY_ALIAS = "MyTaskKeyAlias"

    fun getKeyOrGenerate(): SecretKey {
        val ks = KeyStore.getInstance(ANDROID_KEY_STORE).apply { load(null) }
        return if(isKeyExists(ks)) {
            val secretKeyEntry = ks.getEntry(MY_TASK_KEY_ALIAS, null) as KeyStore.SecretKeyEntry
            secretKeyEntry.secretKey
        }else {
            generateSymmetricKey()
        }
    }

    private fun generateSymmetricKey():SecretKey {
        val keyGenerator = KeyGenerator.getInstance(
            KeyProperties.KEY_ALGORITHM_AES,
            ANDROID_KEY_STORE
        )

        val keyGenParamSpec = KeyGenParameterSpec.Builder(
            MY_TASK_KEY_ALIAS,
            KeyProperties.PURPOSE_ENCRYPT or
                    KeyProperties.PURPOSE_DECRYPT
        ).setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .setKeySize(256)
            .build()

        keyGenerator.init(keyGenParamSpec)
        return keyGenerator.generateKey()
    }

    private fun isKeyExists(keyStore: KeyStore): Boolean {
        return keyStore.containsAlias(MY_TASK_KEY_ALIAS)
    }
}