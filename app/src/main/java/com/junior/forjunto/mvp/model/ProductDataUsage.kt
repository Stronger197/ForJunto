package com.junior.forjunto.mvp.model

import android.content.Context
import android.database.DatabaseUtils
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.google.gson.Gson
import com.junior.forjunto.App
import com.junior.forjunto.mvp.presenter.IProductListPresenter
import com.junior.forjunto.network.RetrofitModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductDataUsage(val productListPresenter: IProductListPresenter) {
    private val TAG: String = "Products model"
    private var dbHelper = DBHelper(App.getContextApp())

    /** invoke this function if you want update product list from server */
    fun updateProducts(category: String) {
        getProducts(category)
    }

    /**
     * this function getting products from producthunt api, cache it in memory
     * if producthunt api is unreachable data will be retrieved from cache
     * When is done function invoke callback from presenter class
     */
    private fun getProducts(category: String) {
        productListPresenter.productListUpdating()
        RetrofitModel.getApi().getProducts(category).enqueue(object : Callback<ProductHuntProductsApiResponse> {
            override fun onResponse(call: Call<ProductHuntProductsApiResponse>, response: Response<ProductHuntProductsApiResponse>) {
                val body = response.body()
                Log.d(TAG, "Response CODE: " + response.code())
                Log.d(TAG, "Response Body: " + response.body().toString())
                if (body != null) {
                    cacheProductsList(body.posts, category)
                }
            }

            override fun onFailure(call: Call<ProductHuntProductsApiResponse>, t: Throwable) {
                Log.d(TAG, "error: " + t.message)
                Log.d(TAG, "Connection error ${t.message}\n\t[Loading data from cache...]")
                getProductListFromCache(category)
                productListPresenter.productListUpdatingError()
            }
        })
    }


    /** Saving products data into cache */
    private fun cacheProductsList(posts: List<Product>?, category: String) {
        val db = dbHelper.writableDatabase

        if (posts != null) {
            val sqlEscapeId = DatabaseUtils.sqlEscapeString(category)
            val sqlEscapeObj = DatabaseUtils.sqlEscapeString(Gson().toJson(posts))
            db.execSQL("replace into Products (id, obj) values ($sqlEscapeId ,$sqlEscapeObj);")
        } else {
            productListPresenter.productListUpdatingError()
        }

        db.close()
        getProductListFromCache(category)
    }


    /**
     * function to get list of Products by category from cache
     * when is done will be called callback functions
     */
    private fun getProductListFromCache(category: String) {
        val db = dbHelper.writableDatabase
        val c = db.query("Products", arrayOf("obj"), "id == \"" + category + "\"", null, null, null, null)

        if (c.moveToFirst()) {
            val data = Gson().fromJson("{posts=${c.getString(0)}}", ProductHuntProductsApiResponse::class.java)
            productListPresenter.productListUpdated(data, category)
        } else {
            Log.d(TAG, "0 rows")
            productListPresenter.productListUpdated(ProductHuntProductsApiResponse(listOf()), category)
        }

        c.close()
        db.close()
    }

    internal inner class DBHelper(context: Context) : SQLiteOpenHelper(context, "ProducthuntDB", null, 1) {
        override fun onCreate(db: SQLiteDatabase) {
            db.execSQL("CREATE TABLE Categories ( id text primary key, obj text);")
            db.execSQL("CREATE TABLE Products ( id text primary key, obj text);")
        }

        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}
    }

}