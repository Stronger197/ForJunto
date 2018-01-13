package com.junior.forjunto.mvp.model

import android.content.Context
import com.junior.forjunto.App


object PreferencesUsage {

    private val sharedPreferences = App.getContextApp().getSharedPreferences("preferences", Context.MODE_PRIVATE)

    /** cache a selected category */
    fun setSelectedCategory(categoryName: String) {
        val editor = sharedPreferences!!.edit()
        editor.putString("category", categoryName)
        editor.apply()
    }

    /** get category from cache */
    fun getSelectedCategory(): String {
        return sharedPreferences!!.getString("category", "Tech")
    }


}