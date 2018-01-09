package com.junior.forjunto.activity

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.google.gson.Gson
import com.junior.forjunto.ProductRecyclerViewAdapter
import com.junior.forjunto.R
import com.junior.forjunto.mvp.model.Post
import com.junior.forjunto.mvp.presenter.ProductListPresenter
import com.junior.forjunto.mvp.view.ProductListView
import kotlinx.android.synthetic.main.activity_product_list.*
import kotlinx.android.synthetic.main.content_product_list.*
import kotlinx.android.synthetic.main.product_list_item.view.*



class ProductListActivity : MvpAppCompatActivity(), ProductListView, AdapterView.OnItemSelectedListener,
        SwipeRefreshLayout.OnRefreshListener {

    @InjectPresenter
    lateinit var productListPresenter: ProductListPresenter

    private val spinner by lazy {
        categorySpinner.apply {
            adapter = this@ProductListActivity.adapter
            prompt = "Tech"
            //  setSelection(0)
            onItemSelectedListener = this@ProductListActivity
        }
    }

    private val adapter by lazy {
        ArrayAdapter(this, R.layout.simple_spinner_item, data).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        }
    }

    private var data = arrayListOf("Tech")
    private var productData = mutableListOf<Post>()
    private var mLayoutManager: RecyclerView.LayoutManager? = null
    private var rvAdapter: RecyclerView.Adapter<*>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_list)
        init()
    }

    private fun init() {
        Log.d("Inasd", "asdasd")
        data = arrayListOf(productListPresenter.selectedCategory)
        swiperefresh!!.setOnRefreshListener(this)
        rvAdapter = ProductRecyclerViewAdapter(productData, myListener)
        mLayoutManager = LinearLayoutManager(this)
        product_list_recycler_view
        configureRecyclerView()
        refreshButton?.setOnClickListener(myListener)
    }

    override fun changeActivityToProduct(product: Post) {
        val intent = Intent(this, ProductActivity::class.java)

        val gson = Gson()
        intent.putExtra("product", gson.toJson(product))

        startActivity(intent)
    }

    override fun endRefresh() {
        swiperefresh?.isRefreshing = false
    }

    override fun onRefresh() {
        productListPresenter.productListRefresh()
        Log.d("Refresh", "Invoke")
    }

    // update a list of products
    override fun updateProductList(data: List<Post>) {
        data.forEach { theme -> Log.d("Products", theme.name) }
        productData.clear()
        productData = data.toMutableList()
        product_list_recycler_view!!.adapter = ProductRecyclerViewAdapter(productData, myListener)
        product_list_recycler_view!!.adapter.notifyDataSetChanged()
    }

    // show an snackbar with message
    override fun showSnackBarMessage(message: String) {
        Snackbar.make(layout_product_list, message, Snackbar.LENGTH_LONG).setAction("Ok", null).show()
    }


    private fun configureRecyclerView() {
        product_list_recycler_view!!.layoutManager = mLayoutManager
        product_list_recycler_view!!.adapter = rvAdapter
    }

    // update a categories list
    override fun updateTopicList(data: Set<String>) {

        val selectedItem = if (spinner!!.selectedItem != null) {
            spinner!!.selectedItem.toString()
        } else {
            "Tech"
        }

        this.data.clear()
        data.forEach { this.data.add(it) }
        val index = data.indexOf(selectedItem)

        adapter.notifyDataSetChanged()
        spinner!!.setSelection(index)
    }


    override fun onNothingSelected(parent: AdapterView<*>?) {
        Log.d("NOTHING SELECTED", "INVOKE")
    }

    // this method will be called when new category selected
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        productListPresenter.newCategorySelected(spinner!!.selectedItem.toString())
        Log.d("CATEGORY SELECTED", "INVOKE")
    }

    override fun setErrorRefreshMessageVisibility(isVisible: Boolean) {
        if (isVisible) {
            error_message_layout.visibility = View.VISIBLE
        } else {
            error_message_layout.visibility = View.GONE
        }
    }

    override fun setSwipeRefreshVisibility(isVisible: Boolean) {
        if (isVisible) {
            swiperefresh.visibility = View.VISIBLE
        } else {
            swiperefresh.visibility = View.GONE
        }
    }

    override fun setRecyclerViewVisibility(isVisible: Boolean) {
        if (isVisible) {
            product_list_recycler_view.visibility = View.VISIBLE
        } else {
            product_list_recycler_view.visibility = View.GONE
        }
    }

    private val myListener = View.OnClickListener { v ->
        when (v) {
            refreshButton -> productListPresenter.refreshButtonClicked()
            else -> productListPresenter.newProductSelected(v.product_name_text_view.text.toString())
        }

    }

    override fun setProgressVisibility(isVisible: Boolean) {
        if (isVisible) {
            loading_progress_bar.visibility = View.VISIBLE
        } else {
            loading_progress_bar.visibility = View.GONE
        }
    }

    override fun setNothingImageVisibility(isVisible: Boolean) {
        if (isVisible) {
            nothing_image_view.visibility = View.VISIBLE
        } else {
            nothing_image_view.visibility = View.GONE
        }
    }

}
