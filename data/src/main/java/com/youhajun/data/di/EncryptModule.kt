package com.youhajun.data.di

import com.youhajun.data.encrypt.Crypto
import com.youhajun.data.encrypt.EncryptUtil
import com.youhajun.data.encrypt.KeyStoreUtil
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class EncryptModule {

    @Binds
    abstract fun bindCrypto(crypto: EncryptUtil): Crypto

    companion object {

        @Provides
        fun provideKeyStoreUtil(): KeyStoreUtil {
            return KeyStoreUtil
        }
    }
}