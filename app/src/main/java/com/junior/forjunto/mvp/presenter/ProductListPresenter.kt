package com.junior.forjunto.mvp.presenter

import android.util.Log
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.junior.forjunto.mvp.model.*
import com.junior.forjunto.mvp.view.ProductListView


@InjectViewState
class ProductListPresenter : MvpPresenter<ProductListView>(), IProductListPresenter {
    private val TAG = "ProductListPresenter"

    private var topicListModel = CategoriesDataUsage(this)
    private var productListModel = ProductDataUsage(this)

    private var usedCategory = ""
    private var selectedCategory = PreferencesUsage.getSelectedCategory()
    private var categoriesMap = mutableMapOf<String, Category>()
    private var productMap = mutableMapOf<String, Product>()

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        updateCategories()
    }

    /**
     * getting a list of categories from server or if server is unreachable from the cache
     * on finish will call productListUpdated method
     */
    private fun updateCategories() {
        topicListModel.updateCategories()
        viewState.setProgressVisibility(true)
    }

    /** invoke this function when user select a product */
    fun newProductSelected(topicName: String) {
        viewState.changeActivityToProduct(productMap[topicName]!!)
    }

    /**
     * on new category selected
     * if selected category not equal used category (lifecycle fighter)
     * set selected category into cache and set new used category,
     * after it update product list dataset
     */
    fun newCategorySelected(categoryName: String) {
        Log.d("Selected Category", "selectedCategory is: $categoryName")
        Log.d("Selected Category", "usedCategory is: $usedCategory")

        /**
         * this is orientation changes handler
         * because when orientation changing, the method newCategorySelected is called again
         */
        if (categoryName != usedCategory) {
            selectedCategory = categoryName // set new selected category
            PreferencesUsage.setSelectedCategory(selectedCategory) // save it into cache
            usedCategory = categoryName // set new used category

            viewState.setRecyclerViewVisibility(false) // hide product list
            viewState.setProgressVisibility(true) // show progress bar

            productListModel.updateProducts(categoriesMap[categoryName]?.slug!!) // get product list from server
        }
    }

    // ___________________PRODUCTS _________________________
    /** This method will be called if the product list has not been updated and data will be retrieved from cache */
    override fun productListUpdatingError() {
        viewState.endRefresh()
        viewState.showSnackBarMessage("Connection error. Getting data from cache...")
    }

    /** When product list is updating */
    override fun productListUpdating() {
        viewState.setNothingImageVisibility(false)
    }

    /** This method will be called if the product list successfully updated */
    override fun productListUpdated(data: ProductHuntProductsApiResponse, name: String) {

        /** save product list into array */
        productMap.clear()
        data.posts?.forEach { product ->
            productMap.put(product.name!!, product)
        }

        /** if product list is empty show empty image */
        if (productMap.isEmpty()) {
            Log.d(TAG, "Product Map is empty")
            viewState.setNothingImageVisibility(true)
        }

        viewState.setRecyclerViewVisibility(true) // show product list
        viewState.setProgressVisibility(false) // hide progress bar
        viewState.updateProductList(data.posts!!) // update product list dataset
        viewState.endRefresh()   // hide progress bar
    }
    // _________________________________________________________

    // ___________________CATEGORIES _________________________
    /** This method will be called if the categories list has not been updated and data will be retrieved from cache */
    override fun categoryListUpdatingError() {
        // if cache is empty show error message
        if (categoriesMap.isEmpty()) {
            viewState.setProgressVisibility(false)
            viewState.setErrorRefreshMessageVisibility(true)
        }
    }

    /** this function will be called when the category list will updating */
    override fun categoryListUpdating() {
        viewState.setErrorRefreshMessageVisibility(false)
    }

    /** this function will be called when the category list successfully updated */
    override fun categoryListUpdated(data: List<Category>) {
        categoriesMap.clear()
        data.forEach { category -> categoriesMap.put(category.name!!, category) }
        viewState.setCategoriesData(categoriesMap.keys)
        viewState.setSelectedCategory(selectedCategory)
    }
    // _________________________________________________________


    fun refreshButtonClicked() {
        Log.d(TAG, "Refresh button clicked")
        updateCategories()
    }

    /** invoke this function when you need to update the list of products */
    fun productListRefresh() {
        if (categoriesMap[selectedCategory] != null) {
            productListModel.updateProducts(categoriesMap[selectedCategory]!!.slug!!)
        } else {
            topicListModel.updateCategories()
        }
    }

}