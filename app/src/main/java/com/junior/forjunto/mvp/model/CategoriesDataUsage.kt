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


class CategoriesDataUsage(val productListPresenter: IProductListPresenter) {
    private val TAG: String = "Categories model"
    private var dbHelper: DBHelper? = null

    /** invoke this function if you want update categories list from server */
    fun updateCategories() {
        getCategories()
    }

    /**
     * this function getting categories from producthunt api and cache it in memory
     * if producthunt api is unreachable data will be retrieved from cache
     * When is done function invoke callback from presenter class
     */
    private fun getCategories() {
        Log.d(TAG, "getCategories Invoke")
        productListPresenter.categoryListUpdating()
        RetrofitModel.getApi().getTopics().enqueue(object : Callback<ProductHuntTopicsApiResponse> {
            override fun onResponse(call: Call<ProductHuntTopicsApiResponse>, response: Response<ProductHuntTopicsApiResponse>) {
                val body = response.body()
                Log.d(TAG, "Response CODE: " + response.code())
                Log.d(TAG, "Response Body: " + response.body().toString())
                if (body != null) {
                    cacheCategoriesList(body.topics!!)
                }
            }

            override fun onFailure(call: Call<ProductHuntTopicsApiResponse>, t: Throwable) {
                Log.d(TAG, "error: " + t.message)
                Log.d(TAG, "Connection error ${t.message}\n\t[Loading data from cache...]")
                getCategoriesListFromCache()
                productListPresenter.categoryListUpdatingError()
            }
        })
    }

    /** Saving categories into cache */
    private fun cacheCategoriesList(topics: List<Category>) {
        val db = getDbHelper().writableDatabase

        for (topic in topics) {
            val sqlEscapeId = DatabaseUtils.sqlEscapeString(topic.slug)
            val sqlEscapeObj = DatabaseUtils.sqlEscapeString(Gson().toJson(topic))
            db.execSQL("replace into Categories (id, obj) values ($sqlEscapeId ,$sqlEscapeObj);")
        }

        db.close()
        getCategoriesListFromCache()
    }

    /**
     * function to get list of categories from cache
     * invoke this function if you want update category list from cache
     */
    private fun getCategoriesListFromCache() {
        val db = getDbHelper().writableDatabase
        val c = db.query("Categories", arrayOf("obj"), null, null, null, null, null)
        val data = mutableListOf<Category>()
        if (c.moveToFirst()) {
            do {
                Log.d(TAG, "Obj: " + c.getString(0))
                data.add(Gson().fromJson(c.getString(0), Category::class.java))
            } while (c.moveToNext())
        } else
            Log.d(TAG, "0 rows")

        c.close()
        db.close()

        productListPresenter.categoryListUpdated(data)
    }


    private fun getDbHelper(): DBHelper {
        if (dbHelper == null) {
            dbHelper = DBHelper(App.getContextApp())
        }

        return dbHelper as DBHelper
    }

    internal inner class DBHelper(context: Context) : SQLiteOpenHelper(context, "ProducthuntDB", null, 1) {
        override fun onCreate(db: SQLiteDatabase) {
            db.execSQL("CREATE TABLE Categories ( id text primary key, obj text);")
            db.execSQL("CREATE TABLE Products ( id text primary key, obj text);")
        }

        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

        }
    }

}