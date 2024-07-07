package com.notifgram.core.data_remote.injection

import com.notifgram.core.common.BACKEND_BASE_URL
import com.notifgram.core.common.MyLog.i
import com.notifgram.core.data_remote.networking.channel.ChannelApi
import com.notifgram.core.data_remote.networking.post.PostApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object RetrofitModule {

    @Provides
    @Singleton
    fun provideChannelApi(): ChannelApi {
        return Retrofit.Builder()
            .baseUrl(BACKEND_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BASIC
                    })
                    .addInterceptor(RequestInterceptor)
                    .build()
            )
            .build()
            .create()
    }

    @Provides
    @Singleton
    fun providePostApi(): PostApi {
        return Retrofit.Builder()
            .baseUrl(BACKEND_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BASIC
                    })
                    .addInterceptor(RequestInterceptor)
                    .build()
            )
            .build()
            .create()
    }

}

object RequestInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        i("Outgoing request to ${request.url}")
        return chain.proceed(request)
    }
}