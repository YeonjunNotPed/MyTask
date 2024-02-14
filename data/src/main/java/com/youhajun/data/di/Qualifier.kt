package com.youhajun.data.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MyTaskOkHttpClient

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MyTaskRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class RoomDB