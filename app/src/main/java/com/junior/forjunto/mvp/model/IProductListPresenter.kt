package com.junior.forjunto.mvp.model

interface IProductListPresenter {
    fun categoryListUpdated(data: List<Topic>)
    fun categoryListUpdating()
    fun topicListUpdatingError()
    fun productListUpdated(data: ProductHuntProductsApiResponse, name: String)
    fun productListUpdateError()
}