package com.junior.forjunto.mvp.presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.junior.forjunto.mvp.model.Post
import com.junior.forjunto.mvp.model.PreferencesUsage
import com.junior.forjunto.mvp.view.ProductView


@InjectViewState
class ProductPresenter : MvpPresenter<ProductView>() {
    private var preferencesModel = PreferencesUsage()

    var product: Post? = null

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.getDataFromIntent() // getting data from intent, on finish will call saveProduct function
    }

    fun saveProduct(product: Post) {
        this.product = product

        fillscreen()
    }

    private fun fillscreen() {
        viewState.setImage(product!!.screenshotUrl!!._300px.toString())
        viewState.setDescription(product!!.tagline.toString())
        viewState.setName(product!!.name.toString())
        viewState.setUpvotes(product!!.votesCount!!)
        viewState.setAppbarTitle(preferencesModel.getSelectedCategory())
    }

    fun buttonClick() {
        viewState.openWebPage(product!!.redirectUrl!!)
    }

}