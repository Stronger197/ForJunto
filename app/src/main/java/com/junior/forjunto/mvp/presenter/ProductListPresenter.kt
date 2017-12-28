package com.junior.forjunto.mvp.presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.junior.forjunto.mvp.model.*
import com.junior.forjunto.mvp.view.ProductListView


@InjectViewState
class ProductListPresenter : MvpPresenter<ProductListView>(), IProductListPresenter {


    var selectedCategory = ""
        get() {
            return if (field == "") {
                "Tech"
            } else {
                field
            }
        }

    private var topicListModel = DataUsage(this)
    private var productListModel = ProductDataUsage(this)
    private var preferencesModel = PreferencesUsage()
    private var categoriesMap = mutableMapOf<String, Topic>()
    private var productMap: MutableMap<String, Post>? = null

    init {
        selectedCategory = preferencesModel.getSelectedCategory()
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        // get user category from cache and from internet and call category list updated
        topicListModel.getTopicListFromCache()
        topicListModel.updateTopics()

    }

    override fun productListUpdated(data: ProductHuntProductsApiResponse, name: String) {
        if (productMap == null) productMap = mutableMapOf()
        data.posts?.forEach { product ->
            productMap!!.put(product.name!!, product)
        }
        viewState.updateProductList(data.posts!!)
        viewState.endRefresh()
    }

    fun newTopicSelected(topicName: String) {
        viewState.changeActivityToProduct(productMap!![topicName]!!)
    }

    override fun productListUpdateError() {
        viewState.endRefresh()
        viewState.showSnackBarMessage("Error while updating list, please check your internet connection")
    }

    fun newCategorySelected(categoryName: String) {
        selectedCategory = categoryName
        preferencesModel.setSelectedCategory(selectedCategory)
        productListModel.getProductListFromCache(categoriesMap[categoryName]!!.slug!!)
        productListModel.updateProducts(categoriesMap[categoryName]!!.slug!!)
    }

    fun productListRefresh() {
        if (categoriesMap[selectedCategory] != null) {
            productListModel.updateProducts(categoriesMap[selectedCategory]!!.slug!!)
        } else {
            topicListModel.updateTopics()
        }
    }

    override fun topicListUpdatingError() {
        // TODO show error message
    }

    override fun categoryListUpdating() {
        viewState.showAppbarProgressBar()
    }

    override fun categoryListUpdated(data: List<Topic>) {
        categoriesMap.clear()
        data.forEach { category -> categoriesMap.put(category.name!!, category) }

        viewState.updateTopicList(categoriesMap.keys)
        viewState.hideAppbarProgressBar()
    }

}