package com.junior.forjunto.mvp.model

import android.content.Context
import android.content.SharedPreferences
import com.junior.forjunto.App


class PreferencesUsage {

    private var sharedPreferences: SharedPreferences? = null
        get() {
            if (field == null) {
                field = App.getContextApp().getSharedPreferences("preferences", Context.MODE_PRIVATE)
            }

            return field
        }


    fun setSelectedCategory(categoryName: String) {
        val editor = sharedPreferences!!.edit()
        editor.putString("category", categoryName)
        editor.apply()
    }

    fun getSelectedCategory(): String {
        return sharedPreferences!!.getString("category", "Tech")
    }


}