package com.junior.forjunto.mvp.presenter

import android.util.Log
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.junior.forjunto.mvp.model.*
import com.junior.forjunto.mvp.view.ProductListView


@InjectViewState
class ProductListPresenter : MvpPresenter<ProductListView>(), IProductListPresenter {

    var selectedCategory = PreferencesUsage.getSelectedCategory()
    private var topicListModel = DataUsage(this)
    private var productListModel = ProductDataUsage(this)
    private var categoriesMap = mutableMapOf<String, Topic>()
    private var productMap: MutableMap<String, Post>? = null

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        // getting category list from cache and from server
        // on finish will call productListUpdated method
        initialization()
    }

    private fun initialization() {
        topicListModel.getCategoriesListFromCache()
        topicListModel.updateCategories()
    }

    // This method will be called after data in cache successfully updated
    override fun productListUpdated(data: ProductHuntProductsApiResponse, name: String) {
        if (productMap == null) productMap = mutableMapOf()
        data.posts?.forEach { product ->
            productMap!!.put(product.name!!, product)
        }
        viewState.updateProductList(data.posts!!)   // update category spinner in toolbar
        viewState.endRefresh()   // hide progress bar
    }

    // invoke this function when user select a product
    fun newProductSelected(topicName: String) {
        viewState.changeActivityToProduct(productMap!![topicName]!!)
    }

    // This method will be called if the product list has not been updated
    override fun productListUpdateError() {
        viewState.endRefresh()
    }

    // invoke this function when the user select a category
    fun newCategorySelected(categoryName: String) {
        selectedCategory = categoryName
        PreferencesUsage.setSelectedCategory(selectedCategory)
        productListModel.getProductListFromCache(categoriesMap[categoryName]!!.slug!!) // getting product list from cache if it possible
        productListModel.updateProducts(categoriesMap[categoryName]!!.slug!!) // getting product list from server
    }

    // invoke this function when you need to update the list of products
    fun productListRefresh() {
        if (categoriesMap[selectedCategory] != null) {
            productListModel.updateProducts(categoriesMap[selectedCategory]!!.slug!!)
        } else {
            topicListModel.updateCategories()
        }
    }

    override fun categoryListUpdatingError() {
        Log.d("CategoryList", "Error")
        if (categoriesMap.isEmpty()) {
            viewState.showErrorRefreshMessage(true)
        }
    }

    // this function will be called when the category list will updating
    override fun categoryListUpdating() {
        viewState.showErrorRefreshMessage(false)
    }

    // this function will be called when the category list will updated
    override fun categoryListUpdated(data: List<Topic>) {
        categoriesMap.clear()
        data.forEach { category -> categoriesMap.put(category.name!!, category) }
        viewState.updateTopicList(categoriesMap.keys)
    }

    fun refreshButtonClicked() {
        initialization()
        Log.d("REFRESH", "BUTTON CLICKED")
    }

}