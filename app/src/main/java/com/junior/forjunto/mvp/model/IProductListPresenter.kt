package com.junior.forjunto.mvp.model

/**
 * Created by stronger197 on 12/25/17.
 */
interface IProductListPresenter {
    fun topicListUpdated(data: List<Topic>)
    fun topicListUpdating()
    fun topicListUpdatingError()
    fun productListUpdated(data: ProductHuntProductsApiResponse)
}