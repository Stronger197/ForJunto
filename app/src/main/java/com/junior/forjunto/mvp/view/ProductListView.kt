package com.junior.forjunto.mvp.view

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.junior.forjunto.mvp.model.Post

interface ProductListView : MvpView {
    fun updateTopicList(data: Set<String>)
    fun updateProductList(data: List<Post>)
    fun endRefresh()

    @StateStrategyType(SkipStrategy::class)
    fun changeActivityToProduct(product: Post)

    @StateStrategyType(SkipStrategy::class)
    fun showSnackBarMessage(message: String)

    fun setErrorRefreshMessageVisibility(isVisible: Boolean)
    fun setSwipeRefreshVisibility(isVisible: Boolean)
    fun setRecyclerViewVisibility(isVisible: Boolean)
    fun setProgressVisibility(isVisible: Boolean)
    fun setNothingImageVisibility(isVisible: Boolean)


}