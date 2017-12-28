package com.junior.forjunto.activity

import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ProgressBar
import android.widget.Spinner
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

    private var spinner: Spinner? = null
        get() {
            if (field == null) {
                field = categorySpinner
                // do this for each of your text views
                field!!.adapter = adapter
                // заголовок
                field!!.prompt = "Tech"

                field!!.setSelection(0)

                field!!.onItemSelectedListener = this


            }
            return field as Spinner
        }

    private var adapter: ArrayAdapter<String>? = null
        get() {
            if (field == null) {
                field = ArrayAdapter(this, R.layout.simple_spinner_item, data)
                field!!.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                field!!.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
            }

            return field as ArrayAdapter<String>
        }

    private var progressBar: ProgressBar? = null
    private var data = arrayListOf("Tech")
    private var productData = mutableListOf<Post>()
    private var mRecyclerView: RecyclerView? = null
    private var mLayoutManager: RecyclerView.LayoutManager? = null
    private var rvAdapter: RecyclerView.Adapter<*>? = null
    private var swipeRefreshLayout: SwipeRefreshLayout? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_list)
        init()
    }

    private fun init() {
        data = arrayListOf(productListPresenter.selectedCategory)
        swipeRefreshLayout = swiperefresh
        swipeRefreshLayout!!.setOnRefreshListener(this)
        spinner
        adapter
        progressBar = topicsProgressBar
        progressBar!!.indeterminateDrawable.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN)
        rvAdapter = ProductRecyclerViewAdapter(productData, myListener)
        mLayoutManager = LinearLayoutManager(this)
        mRecyclerView = product_list_recycler_view
        configureRecyclerView()
    }

    override fun changeActivityToProduct(product: Post) {
        val intent = Intent(this, ProductActivity::class.java)

        val gson = Gson()
        intent.putExtra("product", gson.toJson(product))

        startActivity(intent)
    }

    override fun endRefresh() {
        swipeRefreshLayout!!.isRefreshing = false
    }

    override fun onRefresh() {
        productListPresenter.productListRefresh()
    }

    // update a list of products
    override fun updateProductList(data: List<Post>) {
        data.forEach { theme -> Log.d("Products", theme.name) }
        productData.clear()
        productData = data.toMutableList()
        mRecyclerView!!.adapter = ProductRecyclerViewAdapter(productData, myListener)
        mRecyclerView!!.adapter.notifyDataSetChanged()
    }

    // show an snackbar with message
    override fun showSnackBarMessage(message: String) {
        Snackbar.make(layout_product_list, message, Snackbar.LENGTH_LONG).setAction("Ok", null).show()
    }


    private fun configureRecyclerView() {
        mRecyclerView!!.layoutManager = mLayoutManager
        mRecyclerView!!.adapter = rvAdapter
    }

    override fun showAppbarProgressBar() {
        progressBar!!.visibility = View.VISIBLE
    }

    override fun hideAppbarProgressBar() {
        progressBar!!.visibility = View.GONE
    }

    // update a categories list
    override fun updateTopicList(data: Set<String>) {

        val selectedItem: String = if (spinner!!.selectedItem != null) {
            spinner!!.selectedItem.toString()
        } else {
            "Tech"
        }
        this.data.clear()
        data.forEach { item -> this.data.add(item) }
        val index = data.indexOf(selectedItem)

        adapter!!.notifyDataSetChanged()
        spinner!!.setSelection(index)
    }


    override fun onNothingSelected(parent: AdapterView<*>?) {
        // TODO on nothing selected
    }

    // this method will be called when new category selected
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        productListPresenter.newCategorySelected(spinner!!.selectedItem.toString())
    }

    private val myListener = View.OnClickListener { v ->
        productListPresenter.newTopicSelected(v.product_name_text_view.text.toString())
    }

}