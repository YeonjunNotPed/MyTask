package com.youhajun.data.di

import com.youhajun.data.bridge.RemoteModuleBridgeImpl
import com.youhajun.remote.RemoteModuleBridge
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class BridgeModule {

    @Binds
    @Singleton
    abstract fun bindRemoteModuleBridge(remoteModuleBridge: RemoteModuleBridgeImpl): RemoteModuleBridge
}