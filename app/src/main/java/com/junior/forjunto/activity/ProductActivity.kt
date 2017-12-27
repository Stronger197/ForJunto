package com.junior.forjunto.activity


import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.google.gson.Gson
import com.junior.forjunto.R
import com.junior.forjunto.mvp.model.Post
import com.junior.forjunto.mvp.presenter.ProductPresenter
import com.junior.forjunto.mvp.view.ProductView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_product.*

class ProductActivity : MvpAppCompatActivity(), ProductView {
    override fun setDescription(description: String) {
        descriptionTV!!.text = description
    }

    override fun setName(name: String) {
        nameTV!!.text = name
    }

    override fun setUpvotes(num: Int) {
        upvoteTV!!.text = "Upvote $num"
    }

    override fun setImage(url: String) {
        Picasso.with(this).load(url).fit().into(imageView)
    }

    override fun getDataFromIntent() {
        var intent = intent
        var gson = Gson()

        productPresenter.saveProduct(gson.fromJson(intent.getStringExtra("product"), Post::class.java))
    }

    @InjectPresenter
    lateinit var productPresenter: ProductPresenter

    private var imageView: ImageView? = null
        get() {
            if (field == null) {
                field = product_image_view
            }

            return field
        }

    private var descriptionTV: TextView? = null
        get() {
            if (field == null) {
                field = product_description
            }

            return field
        }

    private var nameTV: TextView? = null
        get() {
            if (field == null) {
                field = product_name
            }

            return field
        }

    private var upvoteTV: TextView? = null
        get() {
            if (field == null) {
                field = upvotes
            }

            return field
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)
        imageView
        descriptionTV
        nameTV
        upvoteTV

    }


}