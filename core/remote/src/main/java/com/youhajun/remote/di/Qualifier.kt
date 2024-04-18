package com.youhajun.remote.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class GptOkHttpClient

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ChatGptRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class GeminiRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MyTaskWebSocketOkHttpClient

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MyTaskOkHttpClient

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MyTaskRetrofit