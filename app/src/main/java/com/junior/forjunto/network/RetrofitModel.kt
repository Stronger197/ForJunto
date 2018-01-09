package com.junior.forjunto.network

import com.junior.forjunto.productHuntToken
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit


object RetrofitModel {
    fun getApi(): ProductHuntApi {
        return configureRetrofit().create(ProductHuntApi::class.java)
    }

    private fun configureRetrofit(): Retrofit {
        val client = OkHttpClient.Builder()
                .writeTimeout(1, TimeUnit.SECONDS)
                .connectTimeout(1, TimeUnit.SECONDS)
                .readTimeout(2, TimeUnit.SECONDS)
                .addInterceptor(TokenInterceptor())
                .build()

        return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .baseUrl("https://api.producthunt.com")
                .build()
    }

    private class TokenInterceptor : Interceptor {

        val token = productHuntToken

        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            val initialRequest = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer $token")
                    .build()

            return chain.proceed(initialRequest)
        }
    }

}