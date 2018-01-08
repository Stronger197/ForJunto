package com.junior.forjunto.mvp.model

import android.content.Context
import android.database.DatabaseUtils
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.google.gson.Gson
import com.junior.forjunto.App
import com.junior.forjunto.mvp.presenter.ProductListPresenter
import com.junior.forjunto.network.RetrofitModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DataUsage(productListPresenter: ProductListPresenter) {
    private val TAG: String = "Producthunt API"
    private var productListPresenterInterface: IProductListPresenter? = null
    private var dbHelper: DBHelper? = null

    init {
        productListPresenterInterface = productListPresenter
    }

    // invoke this function if you want update category list from server
    fun updateCategories() {
        getCategories()
    }

    // this function getting topics from product hunt api, cache it in memory
    // When is done function invoke callback from presenter class
    private fun getCategories() {
        Log.d(TAG, "getCategories Invoke")
        productListPresenterInterface!!.categoryListUpdating()
        RetrofitModel.getApi().getTopics().enqueue(object : Callback<ProductHuntTopicsApiResponse> {
            override fun onResponse(call: Call<ProductHuntTopicsApiResponse>, response: Response<ProductHuntTopicsApiResponse>) {
                val body = response.body()
                Log.d(TAG, "Response CODE: " + response.code())
                Log.d(TAG, "Response Body: " + response.body().toString())
                if (body != null) {
                    cacheCategoriesList(body.topics!!)
                } else {
                    productListPresenterInterface!!.categoryListUpdatingError()
                }
            }

            override fun onFailure(call: Call<ProductHuntTopicsApiResponse>, t: Throwable) {
                Log.d(TAG, "error: " + t.message)
                Log.d("TEST", "ERROR FROM DATAUSAGE")
                productListPresenterInterface!!.categoryListUpdatingError()
            }
        })
    }

    // Saving topics data in cache
    // key: topic name (slug)
    // value: Topic object
    // and call callback
    private fun cacheCategoriesList(topics: List<Topic>) {
        val db = getDbHelper().writableDatabase
        val gson = Gson()

        for (topic in topics) {
            val sqlEscapeId = DatabaseUtils.sqlEscapeString(topic.slug)
            val sqlEscapeObj = DatabaseUtils.sqlEscapeString(gson.toJson(topic))
            db.execSQL("replace into Topics (id, obj) values ($sqlEscapeId ,$sqlEscapeObj);")
        }

        db.close()
        getCategoriesListFromCache()
    }

    // function to get list of Topics from cache
    // invoke this function if you want update category list from cache
    fun getCategoriesListFromCache() {
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

    internal inner class DBHelper(context: Context) : SQLiteOpenHelper(context, "ProducthuntDB", null, 1) {

        override fun onCreate(db: SQLiteDatabase) {
            db.execSQL("CREATE TABLE Topics ( id text primary key, obj text);")
            db.execSQL("CREATE TABLE Products ( id text primary key, obj text);")
        }

        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

        }
    }


}