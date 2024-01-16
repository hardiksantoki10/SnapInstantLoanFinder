package com.snap.instant.loan.finder.apimodule

import com.intuit.sdp.BuildConfig
import com.orhanobut.hawk.Hawk
import com.snap.instant.loan.finder.activity.base.UserLoginDetail
import com.snap.instant.loan.finder.apimodule.exception.ResultCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class ApiProvider {
    private val TIMEOUT = 20
    private val BASE = "https://snapinstant.shreenathjitools.com/api/v1/"

    @Provides
    @Singleton
    fun provideRetrofitInst(): Retrofit {
        val logging = HttpLoggingInterceptor()
        if (BuildConfig.DEBUG) logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        else logging.setLevel(HttpLoggingInterceptor.Level.NONE)

        val client = OkHttpClient.Builder()

            .connectTimeout(TIMEOUT.toLong(), TimeUnit.SECONDS)
            .readTimeout(TIMEOUT.toLong(), TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT.toLong(), TimeUnit.SECONDS)
            .pingInterval(200, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true).apply {
                if (BuildConfig.DEBUG) this.addInterceptor(logging)
                this.addInterceptor(Interceptor { chain ->
                    val newRequest: Request = chain.request().newBuilder()
                        .addHeader("Authorization", "Bearer ${Hawk.get(UserLoginDetail.REMEMBER_TOKEN,"")}")
                        .build()
                    chain.proceed(newRequest)
                })
            }.build()

        return Retrofit.Builder().baseUrl(BASE).addConverterFactory(GsonConverterFactory.create()).addCallAdapterFactory(
            ResultCallAdapterFactory()
        ).client(client).build()
    }

    @Provides
    @Singleton
    fun provideQuotifyApi(retrofit: Retrofit): Api {
        return retrofit.create(Api::class.java)
    }

}