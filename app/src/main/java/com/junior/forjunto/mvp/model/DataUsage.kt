package com.junior.forjunto.mvp.model

import android.content.Context
import android.database.DatabaseUtils
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.google.gson.Gson
import com.junior.forjunto.App
import com.junior.forjunto.mvp.presenter.ProductListPresenter
import com.junior.forjunto.network.ProductHuntTopicsApi
import com.junior.forjunto.productHuntToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class DataUsage(productListPresenter: ProductListPresenter) {
    private val TAG: String = "Producthunt API"
    private var productListPresenterInterface: IProductListPresenter? = null
    private var productHuntTopicsApi: ProductHuntTopicsApi? = null
    private var retrofit: Retrofit? = null
    private var dbHelper: DBHelper? = null

    init {
        productListPresenterInterface = productListPresenter
    }

    // this function get topics from product hunt api, cache it in memory
    // When is done function invoke callback from presenter class
    fun updateTopics() {
        getTopics()
    }

    // This function using product hunt API to get list of topics
    // When is done all data caches in local database
    private fun getTopics() {
        Log.d(TAG, "getTopics Invoke")
        productListPresenterInterface!!.categoryListUpdating()
        getApi().getData("Bearer " + productHuntToken).enqueue(object : Callback<ProductHuntTopicsApiResponse> {
            override fun onResponse(call: Call<ProductHuntTopicsApiResponse>, response: Response<ProductHuntTopicsApiResponse>) {
                val body = response.body()
                Log.d(TAG, "Response CODE: " + response.code())
                Log.d(TAG, "Response Body: " + response.body().toString())
                if (body != null) {
                    cacheTopicList(body.topics!!)
                } else {
                    productListPresenterInterface!!.topicListUpdatingError()
                }
            }

            override fun onFailure(call: Call<ProductHuntTopicsApiResponse>, t: Throwable) {
                Log.d(TAG, "error: " + t.message)
                productListPresenterInterface!!.topicListUpdatingError()
            }
        })
    }

    // Saving topics data in cache
    // key: topic name (slug)
    // value: Topic object
    private fun cacheTopicList(topics: List<Topic>) {
        val db = getDbHelper().writableDatabase
        val gson = Gson()

        for (topic in topics) {
            val sqlEscapeId = DatabaseUtils.sqlEscapeString(topic.slug)
            val sqlEscapeObj = DatabaseUtils.sqlEscapeString(gson.toJson(topic))
            db.execSQL("replace into Topics (id, obj) values ($sqlEscapeId ,$sqlEscapeObj);")
        }

        db.close()
        getTopicListFromCache()
    }

    // function to get list of Topics from cache
    fun getTopicListFromCache() {
        val gson = Gson()
        val db = getDbHelper().writableDatabase
        val c = db.query("Topics", arrayOf("obj"), null, null, null, null, null)
        val data = mutableListOf<Topic>()
        if (c.moveToFirst()) {
            do {
                Log.d(TAG, "Obj: " + c.getString(0))
                data.add(gson.fromJson(c.getString(0), Topic::class.java))
            } while (c.moveToNext())
        } else
            Log.d(TAG, "0 rows")

        c.close()
        db.close()

        productListPresenterInterface!!.categoryListUpdated(data)
    }


    private fun getDbHelper(): DBHelper {
        if (dbHelper == null) {
            dbHelper = DBHelper(App.getContextApp())
        }
        return dbHelper as DBHelper
    }

    // returns interface to work with producthunt API
    private fun getApi(): ProductHuntTopicsApi {
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                    .baseUrl("https://api.producthunt.com")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
        }

        if (productHuntTopicsApi == null) {
            productHuntTopicsApi = retrofit!!.create<ProductHuntTopicsApi>(ProductHuntTopicsApi::class.java)
        }

        return productHuntTopicsApi!!
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