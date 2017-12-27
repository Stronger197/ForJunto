package com.junior.forjunto.activity

import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.*
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
    override fun changeActivityToProduct(product: Post) {
        var intent = Intent(this, ProductActivity::class.java)

        var gson = Gson()
        intent.putExtra("product", gson.toJson(product))

        startActivity(intent)
    }

    override fun endRefresh() {
        swipeRefreshLayout!!.isRefreshing = false
    }

    override fun onRefresh() {
        Log.d("my_tag", "refresh")
        productListPresenter.productListRefresh()
    }

    private val swipeRefreshLayout: SwipeRefreshLayout? = null
        get() {
            if (field == null) {
                field = swiperefresh
                setRefreshListener()
            }

            return field
        }

    private fun setRefreshListener() {
        swipeRefreshLayout!!.setOnRefreshListener(this)
    }

    override fun updateProductList(data: List<Post>) {
        Log.d("UPDATE PRODUCT LIST", "INVOKE")
        data.forEach { theme -> Log.d("UPDATE PRODUCT LIST", theme.name) }
        productData.clear()
        productData = data.toMutableList()
        mRecyclerView!!.adapter = ProductRecyclerViewAdapter(productData, myListener)
        mRecyclerView!!.adapter.notifyDataSetChanged()
    }

    private val mRecyclerView: RecyclerView? = null
        get() {
            if (field == null) {
                field = product_list_recycler_view
                configureRecyclerView()
            }

            return field
        }

    private val mLayoutManager: RecyclerView.LayoutManager? = null
        get() {
            if (field == null) {
                field = LinearLayoutManager(this)
            }

            return field
        }

    private val rvAdapter: RecyclerView.Adapter<*>? = null
        get() {
            if (field == null) {
                field = ProductRecyclerViewAdapter(productData, myListener)
            }


            return field
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

    override fun updateTopicList(data: Set<String>) {

        var selectedItem: String = if (spinner!!.selectedItem != null) {
            spinner!!.selectedItem.toString()
        } else {
            "Tech"
        }
        this.data.clear()
        data.forEach { item -> this.data.add(item) }
        var index = data.indexOf(selectedItem)

        adapter!!.notifyDataSetChanged()
        spinner!!.setSelection(index)
    }


    override fun onNothingSelected(parent: AdapterView<*>?) {
        Log.d("TODO", "TODO")
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        Log.d("SELECTED ITEM", spinner!!.selectedItem.toString())

        productListPresenter.newCategorySelected(spinner!!.selectedItem.toString())

    }

    @InjectPresenter
    lateinit var productListPresenter: ProductListPresenter

    var tv: TextView? = null

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
        get() {
            if (field == null) {
                field = topicsProgressBar
                setNewColorForProgressBar()
            }

            return field
        }

    private fun setNewColorForProgressBar() {
        progressBar!!.indeterminateDrawable.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN)
    }

    var data = arrayListOf("Tech")
    var productData = mutableListOf<Post>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_list)
        Log.d("ProductListActivity", "Invoke")
        swipeRefreshLayout
    }

    private val myListener = View.OnClickListener { v ->
        Log.d("Button", "CLICK")
        productListPresenter.newTopicSelected(v.product_name_text_view.text.toString())
    }

}