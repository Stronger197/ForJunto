package com.junior.forjunto.mvp.presenter

import android.util.Log
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.junior.forjunto.mvp.model.*
import com.junior.forjunto.mvp.view.ProductListView


@InjectViewState
class ProductListPresenter : MvpPresenter<ProductListView>(), IProductListPresenter {
    val TAG = "ProductListPresenter"

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
        topicListModel.updateCategories()
        viewState.setProgressVisibility(true)
    }

    // invoke this function when user select a product
    fun newProductSelected(topicName: String) {
        viewState.changeActivityToProduct(productMap!![topicName]!!)
    }

    // invoke this function when the user select a category
    fun newCategorySelected(categoryName: String) {
        selectedCategory = categoryName
        PreferencesUsage.setSelectedCategory(selectedCategory)
        viewState.setRecyclerViewVisibility(false)
        viewState.setProgressVisibility(true)

        productListModel.updateProducts(categoriesMap[categoryName]!!.slug!!) // getting product list from server
    }

    // This method will be called if the product list has not been updated
    override fun productListUpdatingError() {
        viewState.endRefresh()
        viewState.showSnackBarMessage("Connection error. Getting data from cache")
    }

    // This method will be called if the product list successfully updated
    override fun productListUpdating() {
        viewState.setNothingImageVisibility(false)
    }

    // This method will be called if the product list successfully updated
    override fun productListUpdated(data: ProductHuntProductsApiResponse, name: String) {
        if (productMap == null) productMap = mutableMapOf()
        data.posts?.forEach { product ->
            productMap!!.put(product.name!!, product)
        }

        if (data.posts!!.isEmpty()) {
            Log.d(TAG, "Product Map is empty")
            viewState.setNothingImageVisibility(true)
        }

        viewState.setRecyclerViewVisibility(true)
        viewState.setProgressVisibility(false)
        viewState.updateProductList(data.posts!!)
        viewState.endRefresh()   // hide progress bar
    }


    // ___________________CATEGORIES _________________________
    override fun categoryListUpdatingError() {
        if (categoriesMap.isEmpty()) {
            viewState.setProgressVisibility(false)
            viewState.setErrorRefreshMessageVisibility(true)
        }
    }

    // this function will be called when the category list will updating
    override fun categoryListUpdating() {
        viewState.setErrorRefreshMessageVisibility(false)
    }

    // this function will be called when the category list will updated
    override fun categoryListUpdated(data: List<Topic>) {
        categoriesMap.clear()
        data.forEach { category -> categoriesMap.put(category.name!!, category) }
        viewState.updateTopicList(categoriesMap.keys)
    }
    // _________________________________________________________


    fun refreshButtonClicked() {
        Log.d(TAG, "Refresh button clicked")
        initialization()
    }

    // invoke this function when you need to update the list of products
    fun productListRefresh() {
        if (categoriesMap[selectedCategory] != null) {
            productListModel.updateProducts(categoriesMap[selectedCategory]!!.slug!!)
        } else {
            topicListModel.updateCategories()
        }
    }

}