package com.junior.forjunto.mvp.view

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.junior.forjunto.mvp.model.Product

interface ProductListView : MvpView {
    fun setCategoriesData(data: Set<String>)
    fun updateProductList(data: List<Product>)
    fun endRefresh()

    @StateStrategyType(SkipStrategy::class)
    fun changeActivityToProduct(product: Product)

    @StateStrategyType(SkipStrategy::class)
    fun showSnackBarMessage(message: String)

    fun setErrorRefreshMessageVisibility(isVisible: Boolean)
    fun setSwipeRefreshVisibility(isVisible: Boolean)
    fun setRecyclerViewVisibility(isVisible: Boolean)
    fun setProgressVisibility(isVisible: Boolean)
    fun setNothingImageVisibility(isVisible: Boolean)
    fun setSelectedCategory(categoryName: String)

}