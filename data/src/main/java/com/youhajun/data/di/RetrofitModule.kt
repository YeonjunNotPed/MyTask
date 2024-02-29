package com.youhajun.data.di

import com.google.gson.GsonBuilder
import com.youhajun.data.BuildConfig
import com.youhajun.data.network.AuthenticationInterceptor
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


@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    @Provides
    @MyTaskRetrofit
    fun provideRetrofit(
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
    fun provideGson(): GsonConverterFactory {
        val gson = GsonBuilder().setLenient().create()
        return GsonConverterFactory.create(gson)
    }


    @Provides
    @MyTaskOkHttpClient
    fun provideHttpClient(
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
}