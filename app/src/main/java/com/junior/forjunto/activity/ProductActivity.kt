package com.junior.forjunto.activity

import android.net.Uri
import android.os.Bundle
import android.support.customtabs.CustomTabsIntent
import android.support.v4.content.res.ResourcesCompat
import android.view.MenuItem
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.google.gson.Gson
import com.junior.forjunto.R
import com.junior.forjunto.mvp.model.Product
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
        get_button!!.setOnClickListener({ productPresenter.buttonClick() })
    }

    override fun setAppbarTitle(title: String) {
        supportActionBar?.title = title
    }

    override fun setDescription(description: String) {
        product_description.text = description
    }

    override fun setName(name: String) {
        product_name.text = name
    }

    override fun setUpvotes(num: Int) {
        upvotes.text = getString(R.string.upvote, num)
    }

    override fun setImage(url: String) {
        Picasso.with(this).load(url).fit().into(product_image_view)
    }

    override fun getDataFromIntent() {
        val intent = intent

        productPresenter.saveProduct(Gson().fromJson(intent.getStringExtra("product"), Product::class.java))
    }

    // open web page using chrome custom tabs
    override fun openWebPage(url: String) {
        val builder = CustomTabsIntent.Builder()
        builder.setToolbarColor(ResourcesCompat.getColor(resources, R.color.colorPrimary, null))
        builder.setStartAnimations(this, R.anim.slide_in_right, R.anim.slide_out_left)
        builder.setExitAnimations(this, R.anim.slide_in_left, R.anim.slide_out_right)

        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(this, Uri.parse(url))
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                super.onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }


}