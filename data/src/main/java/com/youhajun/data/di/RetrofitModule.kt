package com.youhajun.data.di

import com.google.gson.GsonBuilder
import com.youhajun.data.BuildConfig
import com.youhajun.data.Endpoint
import com.youhajun.data.network.AuthenticationInterceptor
import com.youhajun.data.network.GptInterceptor
import com.youhajun.data.network.TokenAuthenticator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    @Provides
    @MyTaskRetrofit
    @Singleton
    fun provideMyTaskRetrofit(
        @MyTaskOkHttpClient okHttpClient: OkHttpClient,
        gson: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(gson)
            .build()
    }

    @Provides
    @MyTaskOkHttpClient
    fun provideMyTaskHttpClient(
        authenticator: TokenAuthenticator,
        authenticationInterceptor: AuthenticationInterceptor,
    ): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient.Builder()
            .readTimeout(10, TimeUnit.SECONDS)
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .addInterceptor(authenticationInterceptor)
            .authenticator(authenticator)
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @ChatGptRetrofit
    @Singleton
    fun provideChatGptRetrofit(
        @GptOkHttpClient okHttpClient: OkHttpClient,
        gson: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Endpoint.ChatGpt.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(gson)
            .build()
    }

    @Provides
    @GeminiRetrofit
    @Singleton
    fun provideGeminiRetrofit(
        @GptOkHttpClient okHttpClient: OkHttpClient,
        gson: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Endpoint.Gemini.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(gson)
            .build()
    }

    @Provides
    @GptOkHttpClient
    fun provideGptHttpClient(
        gptInterceptor: GptInterceptor
    ): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient.Builder()
            .addInterceptor(gptInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()
    }


    @Provides
    fun provideGson(): GsonConverterFactory {
        val gson = GsonBuilder().setLenient().create()
        return GsonConverterFactory.create(gson)
    }
}