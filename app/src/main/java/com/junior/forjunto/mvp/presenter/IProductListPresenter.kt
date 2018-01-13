package com.junior.forjunto.mvp.presenter

import com.junior.forjunto.mvp.model.Category
import com.junior.forjunto.mvp.model.ProductHuntProductsApiResponse

interface IProductListPresenter {
    fun categoryListUpdated(data: List<Category>)
    fun categoryListUpdating()
    fun categoryListUpdatingError()
    fun productListUpdated(data: ProductHuntProductsApiResponse, name: String)
    fun productListUpdating()
    fun productListUpdatingError()
}