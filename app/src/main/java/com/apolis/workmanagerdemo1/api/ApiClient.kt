package com.apolis.workmanagerdemo1.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {

    private val myRetrofit by lazy {
        val loggingInterceptor = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        val r = Retrofit.Builder()
            .baseUrl("https://psmobitech.com/nba/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

        r   // return value of lambda expression
    }

    val apiService: ApiService by lazy {
        myRetrofit.create(ApiService::class.java)
    }
}