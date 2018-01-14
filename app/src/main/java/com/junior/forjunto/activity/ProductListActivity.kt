package com.junior.forjunto.activity

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.res.ResourcesCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.google.gson.Gson
import com.junior.forjunto.ProductRecyclerViewAdapter
import com.junior.forjunto.R
import com.junior.forjunto.mvp.model.Product
import com.junior.forjunto.mvp.presenter.ProductListPresenter
import com.junior.forjunto.mvp.view.ProductListView
import kotlinx.android.synthetic.main.activity_product_list.*
import kotlinx.android.synthetic.main.content_product_list.*
import kotlinx.android.synthetic.main.product_list_item.view.*



class ProductListActivity : MvpAppCompatActivity(), ProductListView, AdapterView.OnItemSelectedListener,
        SwipeRefreshLayout.OnRefreshListener {

    @InjectPresenter
    lateinit var productListPresenter: ProductListPresenter

    /** array for dataset of categories */
    private var categoriesDataList = mutableListOf<String>()

    /** spinner of categories */
    private val categoriesSpinner by lazy {
        categorySpinner.apply {
            adapter = this@ProductListActivity.categoriesSpinnerAdapter
            onItemSelectedListener = this@ProductListActivity
        }
    }

    /** adapter for categories spinner */
    private val categoriesSpinnerAdapter by lazy {
        ArrayAdapter(this, R.layout.simple_spinner_item, categoriesDataList).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        }
    }

    /** layout for update product dataset */
    private val swipeRefreshLayout by lazy {
        swiperefresh.apply {
            setOnRefreshListener(this@ProductListActivity)
        }
    }

    /** listener to handle clicks on product */
    private val onProductClickListener = View.OnClickListener { v ->
        productListPresenter.newProductSelected(v.product_name_text_view.text.toString())
    }


    /** list of products */
    private val productListRecyclerView by lazy {
        productListRV.apply {
            layoutManager = LinearLayoutManager(this@ProductListActivity)
            adapter = productsRecyclerViewAdapter
        }
    }

    /** adapter for product list recycler view */
    private val productsRecyclerViewAdapter = ProductRecyclerViewAdapter(listOf(), onProductClickListener)




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_list)
        initUIElements()
    }

    /**
    * init UI elements here
    * set adapters, listeners, and more...
    */
    private fun initUIElements() {
        refreshButton.setOnClickListener({ productListPresenter.refreshButtonClicked() })
        loading_progress_bar.indeterminateDrawable.setColorFilter(ResourcesCompat.getColor(resources, R.color.colorPrimary, null),
                android.graphics.PorterDuff.Mode.SRC_IN)
        categoriesSpinner // invoke variable to init adapters
        productListRecyclerView // invoke variable to init adapters
    }

    /** this method switching activity to product activity and giving extras */
    override fun changeActivityToProduct(product: Product) {
        val intent = Intent(this, ProductActivity::class.java)
        intent.putExtra("product", Gson().toJson(product)) // using to fill product activity UI
        startActivity(intent)
    }

    /** hide progress bar on swiperefresh */
    override fun endRefresh() {
        swipeRefreshLayout?.isRefreshing = false
    }

    /** refresh from swiperefresh layout */
    override fun onRefresh() {
        Log.d("Refresh", "Invoke")
        productListPresenter.productListRefresh()
    }

    /** update a list of products */
    override fun updateProductList(data: List<Product>) {
        data.forEach { theme -> Log.d("Products", theme.name) } // just a logging

        /** set adapter with new dataset to update list of products */
        productListRecyclerView.adapter = ProductRecyclerViewAdapter(data.toMutableList(), onProductClickListener)
    }

    /** show an snackbar with message */
    override fun showSnackBarMessage(message: String) {
        Snackbar.make(layout_product_list, message, Snackbar.LENGTH_LONG).show()
    }

    /**
    * update a categories list
    * save the selected category, after updating dataset, will be selected the saved category
    */
    override fun setCategoriesData(data: Set<String>) {
        val selectedItem = categoriesSpinner?.selectedItem.toString()

        categoriesDataList.clear()
        data.forEach { categoriesDataList.add(it) }

        categoriesSpinnerAdapter.notifyDataSetChanged()
        categoriesSpinner.setSelection(data.indexOf(selectedItem))
    }


    override fun onNothingSelected(parent: AdapterView<*>?) {
        Log.d("onNothingSelected", "Invoke!")
    }

    /** this method will be called when new category selected */
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        productListPresenter.newCategorySelected(categoriesSpinner.selectedItem.toString())
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
            swipeRefreshLayout.visibility = View.VISIBLE
        } else {
            swipeRefreshLayout.visibility = View.GONE
        }
    }

    override fun setRecyclerViewVisibility(isVisible: Boolean) {
        if (isVisible) {
            productListRecyclerView.visibility = View.VISIBLE
        } else {
            productListRecyclerView.visibility = View.GONE
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

    /** if categories data list contains saved in cache category select it else choose Tech */
    override fun setSelectedCategory(categoryName: String) {
        if (categoriesDataList.contains(categoryName)) {
            categoriesSpinner.setSelection(categoriesDataList.indexOf(categoryName))
        } else {
            categoriesSpinner.setSelection(categoriesDataList.indexOf("Tech"))
        }
    }
}
