package com.vipulasri.streamer.ui.home

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vipulasri.streamer.R
import com.vipulasri.streamer.inject.ViewModelFactory
import com.vipulasri.streamer.model.Post
import com.vipulasri.streamer.ui.PostsViewModel
import com.vipulasri.streamer.ui.base.BaseActivity
import com.vipulasri.streamer.ui.details.PostDetailsActivity
import kotlinx.android.synthetic.main.activity_main.*
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

        mAdapter = PostsAdapter(arrayListOf())
        mAdapter.setCallbacks(this)

        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
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
                    Log.e("error", "->${it.message}")
                }
            }
        })
    }

    private fun showLoading(active: Boolean) {

    }

    override fun onPostClick(post: Post) {
        PostDetailsActivity.launchActivity(this, postId = post.getId())
    }
}
