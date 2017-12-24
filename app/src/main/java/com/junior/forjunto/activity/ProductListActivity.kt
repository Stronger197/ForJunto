package com.junior.forjunto.activity

import android.os.Bundle

import android.widget.TextView
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.junior.forjunto.R
import com.junior.forjunto.mvp.presenter.ProductListPresenter
import com.junior.forjunto.mvp.view.ProductListView
import kotlinx.android.synthetic.main.activity_product_list.*

class ProductListActivity : MvpAppCompatActivity(), ProductListView {

    @InjectPresenter
    lateinit var productListPresenter: ProductListPresenter

    var tv: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_list)
        tv = helloText
    }


}