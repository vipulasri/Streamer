package com.vipulasri.streamer.ui

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vipulasri.streamer.R
import com.vipulasri.streamer.inject.ViewModelFactory
import com.vipulasri.streamer.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : BaseActivity() {

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

        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recyclerView.adapter = mAdapter

        mViewModel.loadPosts()
    }

    private fun setupObservers() {
        mViewModel.getPostsObserver().observe(this, Observer { data ->
            data.run {
                showLoading(isLoading)

                response?.let {
                    //Log.d("posts", "->$it")
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
}
