package com.junior.forjunto.mvp.presenter

import android.util.Log
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.junior.forjunto.mvp.model.*
import com.junior.forjunto.mvp.view.ProductListView


@InjectViewState
class ProductListPresenter : MvpPresenter<ProductListView>(), IProductListPresenter {
    override fun productListUpdated(data: ProductHuntProductsApiResponse, name: String) {


        if (productMap == null) productMap = mutableMapOf()

        data.posts?.forEach { product ->
            Log.d("Product1", product.name)
            productMap!!.put(product.name!!, product)
        }

        viewState.updateProductList(data.posts!!)
        viewState.endRefresh()
    }

    fun newTopicSelected(topicName: String) {
        Log.d("NEW TOPIC SELECTED", topicName)
        Log.d("NEW TOPIC SELECTED", productMap.toString())
        Log.d("NEW TOPIC SELECTED", productMap!!.toString())
        viewState.changeActivityToProduct(productMap!![topicName]!!)
    }

    fun newCategorySelected(categoryName: String) {

        selectedCategory = categoryName
        Log.d("NEW CATEGORY SELECTED", "________________________________START_______________________________")
        Log.d("NEW CATEGORY SELECTED", "Category Name $categoryName")
        Log.d("NEW CATEGORY SELECTED", "Category Name from topic ${topicsMap!!.get(categoryName)!!.slug}")
        Log.d("NEW CATEGORY SELECTED", "________________________________END_______________________________")
        productListModel.getProductListFromCache(topicsMap!!.get(categoryName)!!.slug!!)

        productListModel.updateProducts(topicsMap!!.get(categoryName)!!.slug!!)
    }

    fun productListRefresh() {
        productListModel.updateProducts(topicsMap!!.get(selectedCategory)!!.slug!!)
    }

    private var selectedCategory = "Tech"

    override fun topicListUpdatingError() {
        // TODO show error message
    }

    override fun topicListUpdating() {
        viewState.showAppbarProgressBar()
    }

    var topicsMap: MutableMap<String, Topic>? = null
    var productMap: MutableMap<String, Post>? = null

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
    }


}