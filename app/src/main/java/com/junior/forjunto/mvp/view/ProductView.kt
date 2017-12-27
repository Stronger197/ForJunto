package com.junior.forjunto.mvp.view

import com.arellomobile.mvp.MvpView

interface ProductView : MvpView {
    fun getDataFromIntent()
    fun setImage(url: String)
    fun setDescription(description: String)
    fun setName(name: String)
    fun setUpvotes(num: Int)
}