package com.junior.forjunto.mvp.model

import android.content.Context
import android.util.Log
import com.junior.forjunto.mvp.presenter.ProductListPresenter
import com.junior.forjunto.network.ProductHuntTopicsApi
import com.junior.forjunto.productHuntToken
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Retrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.junior.forjunto.App


class DataUsage(productListPresenter: ProductListPresenter) {
    private val TAG : String = "Producthunt API"
    private var productListModel : ProductListModel? = null
    private var productHuntTopicsApi: ProductHuntTopicsApi? = null
    private var retrofit: Retrofit? = null


    init {
        productListModel = productListPresenter

    }

    fun updateTopics() {
        getTopics()
    }





    private fun getTopics() {
        Log.d(TAG, "getTopics Invoke" )
        getApi().getData("Bearer " + productHuntToken).enqueue(object : Callback<ProductHuntApiResponse> {
            override fun onResponse(call: Call<ProductHuntApiResponse>, response: Response<ProductHuntApiResponse>) {
                val body = response.body()
                Log.d(TAG, "Response CODE: " + response.code())
                Log.d(TAG,"Response Body: " + response.body().toString())
                if(body != null) {
                    // TODO add cache logic
                } else {
                    // TODO request error here
                }
            }

            override fun onFailure(call: Call<ProductHuntApiResponse>, t: Throwable) {
                Log.d(TAG, "error: " + t.message);
                // TODO error here
            }
        })
    }




    private fun getApi() : ProductHuntTopicsApi{
        if(retrofit == null) {
            retrofit = Retrofit.Builder()
                    .baseUrl("https://api.producthunt.com")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
        }

        if(productHuntTopicsApi == null) {
            productHuntTopicsApi = retrofit!!.create<ProductHuntTopicsApi>(ProductHuntTopicsApi::class.java)
        }

        return productHuntTopicsApi!!
    }


}