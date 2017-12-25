package com.junior.forjunto

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.util.Log


class App : Application() {


    companion object {
        @SuppressLint("StaticFieldLeak")
        @JvmStatic
        lateinit private var context : Context

        @JvmStatic
        fun getContextApp() : Context {
            return context
        }
    }

    override fun onCreate() {
        super.onCreate()
        Log.d("APP", "Invoke")
        context = applicationContext
    }
}