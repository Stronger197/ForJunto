package com.junior.forjunto.activity

import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.junior.forjunto.R
import com.junior.forjunto.mvp.presenter.ProductListPresenter
import com.junior.forjunto.mvp.view.ProductListView
import kotlinx.android.synthetic.main.activity_product_list.*


class ProductListActivity : MvpAppCompatActivity(), ProductListView, AdapterView.OnItemSelectedListener {
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_list)
        Log.d("ProductListActivity", "Invoke")
    }

}