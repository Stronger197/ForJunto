package com.junior.forjunto.mvp.view

import com.arellomobile.mvp.MvpView
import com.junior.forjunto.mvp.model.Post



interface ProductListView : MvpView {
    fun updateTopicList(data: Set<String>)
    fun updateProductList(data: List<Post>)
    fun endRefresh()
    fun changeActivityToProduct(product: Post)
    fun showSnackBarMessage(message: String)
    fun showErrorRefreshMessage(boolean: Boolean)
    fun showSwipeRefresh(boolean: Boolean)
}