package com.junior.forjunto.mvp.presenter

import android.util.Log
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.junior.forjunto.mvp.model.*
import com.junior.forjunto.mvp.view.ProductListView


@InjectViewState
class ProductListPresenter : MvpPresenter<ProductListView>(), IProductListPresenter {
    override fun productListUpdated(data: ProductHuntProductsApiResponse) {
        data.posts?.forEach { product -> Log.d("Product", product.name) }
    }


    override fun topicListUpdatingError() {
        // TODO show error message
    }

    override fun topicListUpdating() {
        viewState.showAppbarProgressBar()
    }

    var topicsMap: MutableMap<String, Topic>? = null

    private var topicListModel: DataUsage = DataUsage(this)
    private var productListModel: ProductDataUsage = ProductDataUsage(this)

    override fun topicListUpdated(data: List<Topic>) {
        if (topicsMap == null) topicsMap = mutableMapOf()
        topicsMap!!.clear()
        data.forEach { topic -> topicsMap!!.put(topic.name!!, topic) }

        viewState.updateTopicList(topicsMap!!.keys)

        viewState.hideAppbarProgressBar()
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        topicListModel.getTopicListFromCache() // init topics from cache if device offline

        topicListModel.updateTopics() // trying get topics from web

        productListModel.updateProducts("tech")
    }


}