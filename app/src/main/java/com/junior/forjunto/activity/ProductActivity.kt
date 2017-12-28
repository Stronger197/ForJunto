package com.junior.forjunto.activity

import android.net.Uri
import android.os.Bundle
import android.support.customtabs.CustomTabsIntent
import android.support.v4.content.res.ResourcesCompat
import android.view.View
import android.widget.Button
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


    @InjectPresenter
    lateinit var productPresenter: ProductPresenter

    private var imageView: ImageView? = null
    private var descriptionTV: TextView? = null
    private var nameTV: TextView? = null
    private var upvoteTV: TextView? = null
    private var getItButton: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)
        imageView = product_image_view
        descriptionTV = product_description
        nameTV = product_name
        upvoteTV = upvotes
        getItButton = get_button
        getItButton!!.setOnClickListener(onClickListener)
    }

    override fun setAppbarTitle(title: String) {
        supportActionBar?.title = title
    }

    override fun setDescription(description: String) {
        descriptionTV!!.text = description
    }

    override fun setName(name: String) {
        nameTV!!.text = name
    }

    override fun setUpvotes(num: Int) {
        upvoteTV!!.text = getString(R.string.upvote, num)
    }

    override fun setImage(url: String) {
        Picasso.with(this).load(url).fit().into(imageView)
    }

    override fun getDataFromIntent() {
        val intent = intent
        val gson = Gson()

        productPresenter.saveProduct(gson.fromJson(intent.getStringExtra("product"), Post::class.java))
    }

    private val onClickListener = View.OnClickListener { v ->
        productPresenter.buttonClick()
    }

    override fun openWebPage(url: String) {
        val builder = CustomTabsIntent.Builder()
        //builder.setSession(session)
        builder.setToolbarColor(ResourcesCompat.getColor(resources, R.color.colorPrimary, null))
// Application exit animation, Chrome enter animation.
        builder.setStartAnimations(this, R.anim.slide_in_right, R.anim.slide_out_left)
// vice versa
        builder.setExitAnimations(this, R.anim.slide_in_left, R.anim.slide_out_right)

        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(this, Uri.parse(url))
    }




}