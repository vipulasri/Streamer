package com.vipulasri.streamer.ui.base

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import androidx.appcompat.widget.Toolbar
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import com.vipulasri.streamer.R
import com.vipulasri.streamer.extensions.whenNotNull
import dagger.android.support.DaggerAppCompatActivity

@SuppressLint("Registered")
open class BaseActivity : DaggerAppCompatActivity() {

    var toolbar: Toolbar? = null

    //If back button is displayed in action bar, return false
    var isDisplayHomeAsUpEnabled: Boolean
        get() = false
        set(value) {
            if (supportActionBar != null)
                supportActionBar!!.setDisplayHomeAsUpEnabled(value)
        }

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)

        injectViews()

        //Displaying the back button in the action bar
        if (isDisplayHomeAsUpEnabled) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        }
    }

    protected fun injectViews() {
        toolbar = findViewById(R.id.toolbar)
        setupToolbar()
    }

    fun setContentViewWithoutInject(layoutResId: Int) {
        super.setContentView(layoutResId)
    }

    protected fun setupToolbar() {
        whenNotNull(toolbar) {
            setSupportActionBar(it)
        }
    }

    fun setActivityTitle(title: Int) {
        whenNotNull(supportActionBar) {
            it.setTitle(title)
        }
    }

    fun setActivityTitle(title: String) {
        whenNotNull(supportActionBar) {
            it.title = title
        }
    }

    fun getToolbarTitleTextView(): View {
        val childCount = toolbar!!.childCount
        for (i in 0 until childCount) {
            val child = toolbar!!.getChildAt(i)
            if (child is TextView) {
                return child
            }
        }

        return View(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //Menu
        when (item.itemId) {
        //When home is clicked
            android.R.id.home -> {
                onActionBarHomeIconClicked()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun setHomeAsUpIndicator(drawable: Drawable) {
        if (supportActionBar != null)
            supportActionBar!!.setHomeAsUpIndicator(drawable)
    }

    //Method for when home button is clicked
    open fun onActionBarHomeIconClicked() {
        if (isDisplayHomeAsUpEnabled) {
            onBackPressed()
        } else {
            finish()
        }
    }
}
