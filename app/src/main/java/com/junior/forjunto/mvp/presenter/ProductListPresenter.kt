package com.junior.forjunto.mvp.presenter

import android.util.Log
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.junior.forjunto.mvp.model.DataUsage
import com.junior.forjunto.mvp.model.ProductListModel
import com.junior.forjunto.mvp.model.Topic
import com.junior.forjunto.mvp.view.ProductListView


@InjectViewState
class ProductListPresenter : MvpPresenter<ProductListView>(), ProductListModel {
    override fun dataUpdated(data: List<Topic>) {
        Log.d("DATA", "_______________INVOKE________________")
        data.forEach { topic -> Log.d("DATA", topic.slug) }
        Log.d("DATA", "_______________END________________")
    }

    private var productListModel: DataUsage = DataUsage(this)

    init {
        Log.d("ProductListInvoke", "INVOKE")
        productListModel.updateTopics()
    }

    fun getMore(message: String) {
        Log.d("MESSAGE", message)
    }




}