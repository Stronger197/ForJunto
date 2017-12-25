package com.junior.forjunto.network


import com.junior.forjunto.mvp.model.ProductHuntApiResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header


interface ProductHuntTopicsApi {

    @GET("/v1/topics?search%5Btrending%5D=true")
    fun getData(@Header("Authorization") authorization: String): Call<ProductHuntApiResponse>

}