package com.vipulasri.streamer.ui.home

import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vipulasri.streamer.R
import com.vipulasri.streamer.extensions.fetchErrorMessage
import com.vipulasri.streamer.extensions.setGone
import com.vipulasri.streamer.extensions.setVisible
import com.vipulasri.streamer.extensions.showSnackBar
import com.vipulasri.streamer.inject.ViewModelFactory
import com.vipulasri.streamer.model.Post
import com.vipulasri.streamer.ui.PostsViewModel
import com.vipulasri.streamer.ui.base.BaseActivity
import com.vipulasri.streamer.ui.details.PostDetailsActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_error.*
import javax.inject.Inject

class MainActivity : BaseActivity(), PostsAdapter.Callbacks {

    @Inject
    lateinit var mViewModelFactory: ViewModelFactory

    private lateinit var mViewModel: PostsViewModel
    private lateinit var mAdapter: PostsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mViewModel = ViewModelProviders.of(this, mViewModelFactory).get(PostsViewModel::class.java)
        setupObservers()

        swipeRefreshLayout.setColorSchemeColors(
            ContextCompat.getColor(this, R.color.colorAccent),
            ContextCompat.getColor(this, R.color.colorPrimaryDark),
            ContextCompat.getColor(this, R.color.colorAccent)
        )

        swipeRefreshLayout.setOnRefreshListener { mViewModel.loadPosts() }

        mAdapter = PostsAdapter(arrayListOf())
        mAdapter.setCallbacks(this)

        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        recyclerView.adapter = mAdapter

        mViewModel.loadPosts()
    }

    private fun setupObservers() {
        mViewModel.getPostsObserver().observe(this, Observer { data ->
            data.run {

                showLoading(isLoading)

                response?.let {
                    Log.d("posts", "->$it")
                    mAdapter.setData(it)
                }

                error?.let {
                    //Log.e("error", "->${it.message}")
                    showError(fetchErrorMessage(it))
                }
            }
        })
    }

    private fun showLoading(active: Boolean) {
        if(swipeRefreshLayout.isRefreshing) {
            if(!active) swipeRefreshLayout.isRefreshing = false
        } else {
            if(active) {
                progress.setVisible()
                showContent(false)
                view_error.setGone()
            } else {
                progress.setGone()
                showContent(true)
                view_error.setGone()
            }
        }
    }

    private fun showError(message: String) {
        showContent(false)
        view_error.setVisible()
        text_error_message.text = message
        button_error_retry.setOnClickListener { mViewModel.loadPosts() }
    }

    private fun showContent(active: Boolean){
        if(active) {
            swipeRefreshLayout.setVisible()
        } else {
            swipeRefreshLayout.setGone()
        }
    }

    override fun onPostClick(post: Post) {
        PostDetailsActivity.launchActivity(this, postId = post.getId())
    }
}
