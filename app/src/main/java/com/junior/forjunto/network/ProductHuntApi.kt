package com.junior.forjunto.network


import com.junior.forjunto.mvp.model.ProductHuntProductsApiResponse
import com.junior.forjunto.mvp.model.ProductHuntTopicsApiResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path


interface ProductHuntTopicsApi {

    @GET("/v1/topics?search%5Btrending%5D=true")
    fun getData(@Header("Authorization") authorization: String): Call<ProductHuntTopicsApiResponse>

}


interface ProductHuntProductsApi {

    @GET("/v1/categories/{category}/posts")
    fun getData(@Header("Authorization") authorization: String, @Path("category") categoryName: String): Call<ProductHuntProductsApiResponse>

}