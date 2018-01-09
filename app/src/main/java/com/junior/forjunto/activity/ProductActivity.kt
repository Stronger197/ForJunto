package com.junior.forjunto.activity

import android.net.Uri
import android.os.Bundle
import android.support.customtabs.CustomTabsIntent
import android.support.v4.content.res.ResourcesCompat
import android.view.View
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

    @InjectPresenter
    lateinit var productPresenter: ProductPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)
        get_button!!.setOnClickListener(onClickListener)
    }

    override fun setAppbarTitle(title: String) {
        supportActionBar?.title = title
    }

    override fun setDescription(description: String) {
        product_description!!.text = description
    }

    override fun setName(name: String) {
        product_name!!.text = name
    }

    override fun setUpvotes(num: Int) {
        upvotes!!.text = getString(R.string.upvote, num)
    }

    override fun setImage(url: String) {
        Picasso.with(this).load(url).fit().into(product_image_view)
    }

    override fun getDataFromIntent() {
        val intent = intent
        val gson = Gson()

        productPresenter.saveProduct(gson.fromJson(intent.getStringExtra("product"), Post::class.java))
    }

    private val onClickListener = View.OnClickListener {
        productPresenter.buttonClick()
    }

    override fun openWebPage(url: String) {
        val builder = CustomTabsIntent.Builder()
        builder.setToolbarColor(ResourcesCompat.getColor(resources, R.color.colorPrimary, null))
        builder.setStartAnimations(this, R.anim.slide_in_right, R.anim.slide_out_left)
        builder.setExitAnimations(this, R.anim.slide_in_left, R.anim.slide_out_right)

        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(this, Uri.parse(url))
    }




}