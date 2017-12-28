package com.junior.forjunto.mvp.model

import android.content.Context
import android.database.DatabaseUtils
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.google.gson.Gson
import com.junior.forjunto.App
import com.junior.forjunto.mvp.presenter.ProductListPresenter
import com.junior.forjunto.network.ProductHuntProductsApi
import com.junior.forjunto.productHuntToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ProductDataUsage(productListPresenter: ProductListPresenter) {
    private val TAG: String = "Producthunt Products"
    private var productListPresenterInterface: IProductListPresenter? = null
    private var productHuntProductsApi: ProductHuntProductsApi? = null
    private var retrofit: Retrofit? = null
    private var dbHelper: DBHelper? = null

    init {
        productListPresenterInterface = productListPresenter
    }


    // invoke this function if you want update product list from server
    fun updateProducts(category: String) {
        getProducts(category)
    }


    // this function getting products from producthunt api, cache it in memory
    // When is done function invoke callback from presenter class
    private fun getProducts(category: String) {
        getApi().getData("Bearer " + productHuntToken, category).enqueue(object : Callback<ProductHuntProductsApiResponse> {
            override fun onResponse(call: Call<ProductHuntProductsApiResponse>, response: Response<ProductHuntProductsApiResponse>) {
                val body = response.body()
                Log.d(TAG, "Response CODE: " + response.code())
                Log.d(TAG, "Response Body: " + response.body().toString())
                if (body != null) {
                    cacheProductsList(body.posts, category)
                } else {
                    productListPresenterInterface!!.productListUpdateError()
                }
            }

            override fun onFailure(call: Call<ProductHuntProductsApiResponse>, t: Throwable) {
                Log.d(TAG, "error: " + t.message)
                productListPresenterInterface!!.productListUpdateError()
            }
        })
    }


    // Saving topics data in cache
    // key: topic name (slug)
    // value: List of Products object
    // and call callback
    private fun cacheProductsList(posts: List<Post>?, category: String) {
        val db = getDbHelper().writableDatabase
        val gson = Gson()

        if (posts != null) {
            val sqlEscapeId = DatabaseUtils.sqlEscapeString(category)
            val sqlEscapeObj = DatabaseUtils.sqlEscapeString(gson.toJson(posts))
            db.execSQL("replace into Products (id, obj) values ($sqlEscapeId ,$sqlEscapeObj);")
        } else {
            productListPresenterInterface!!.productListUpdateError()
        }

        db.close()
        getProductListFromCache(category)
    }


    // function to get list of Products by category from cache
    // invoke this function if you want update product list from cache
    fun getProductListFromCache(category: String) {
        val gson = Gson()
        val db = getDbHelper().writableDatabase
        val c = db.query("Products", arrayOf("obj"), "id == \"" + category + "\"", null, null, null, null)

        if (c.moveToFirst()) {
            val data = gson.fromJson("{posts=${c.getString(0)}}", ProductHuntProductsApiResponse::class.java)
            productListPresenterInterface?.productListUpdated(data, category)
        } else
            Log.d(TAG, "0 rows")


        c.close()
        db.close()
    }


    private fun getDbHelper(): DBHelper {
        if (dbHelper == null) {
            dbHelper = DBHelper(App.getContextApp())
        }
        return dbHelper as DBHelper
    }

    // returns interface to work with producthunt API
    private fun getApi(): ProductHuntProductsApi {
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                    .baseUrl("https://api.producthunt.com")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
        }

        if (productHuntProductsApi == null) {
            productHuntProductsApi = retrofit!!.create<ProductHuntProductsApi>(ProductHuntProductsApi::class.java)
        }

        return productHuntProductsApi!!
    }

    internal inner class DBHelper(context: Context) : SQLiteOpenHelper(context, "ProducthuntDB", null, 1) {

        override fun onCreate(db: SQLiteDatabase) {
            db.execSQL("CREATE TABLE Topics ( id text primary key, obj text);")
            db.execSQL("CREATE TABLE Products ( id text primary key, obj text);")
        }

        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

        }
    }


}