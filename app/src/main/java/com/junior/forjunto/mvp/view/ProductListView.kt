package com.junior.forjunto.mvp.view

import com.arellomobile.mvp.MvpView


interface ProductListView : MvpView {
    fun updateTopicList(data: Set<String>)
    fun showAppbarProgressBar()
    fun hideAppbarProgressBar()
}