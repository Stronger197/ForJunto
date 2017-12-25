package com.junior.forjunto.mvp.presenter

import android.util.Log
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.junior.forjunto.mvp.model.ProductListModel
import com.junior.forjunto.mvp.model.DataUsage
import com.junior.forjunto.mvp.view.ProductListView


@InjectViewState
class ProductListPresenter : MvpPresenter<ProductListView>(), ProductListModel {

    private var productListModel: DataUsage = DataUsage(this)

    init {
        Log.d("ProductListInvoke", "INVOKE")
        productListModel.updateTopics()
    }

    fun saldk(a: Int) {

    }

    fun getMore(message: String) {
        Log.d("MESSAGE", message)

    }




}