package com.junior.forjunto.network


import com.junior.forjunto.mvp.model.ProductHuntProductsApiResponse
import com.junior.forjunto.mvp.model.ProductHuntTopicsApiResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path


interface ProductHuntApi {

    @GET("/v1/topics?search%5Btrending%5D=true")
    fun getTopics(): Call<ProductHuntTopicsApiResponse>


    @GET("/v1/categories/{category}/posts")
    fun getProducts(@Path("category") categoryName: String): Call<ProductHuntProductsApiResponse>

}