package com.vipulasri.streamer.ui.details

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.vipulasri.streamer.R
import com.vipulasri.streamer.inject.ViewModelFactory
import com.vipulasri.streamer.ui.PostsViewModel
import com.vipulasri.streamer.ui.base.BaseActivity
import javax.inject.Inject
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.DefaultRenderersFactory
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import kotlinx.android.synthetic.main.activity_post_details.*
import com.google.android.exoplayer2.source.MediaSource
import android.net.Uri
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import android.view.View
import com.google.android.exoplayer2.SimpleExoPlayer
import android.os.Build


class PostDetailsActivity : BaseActivity() {

    companion object {
        private const val EXTRA_POST_ID = "EXTRA_POST_ID"

        fun launchActivity(startingActivity: Context, postId: String) {
            val intent = Intent(startingActivity, PostDetailsActivity::class.java).apply {
                putExtra(EXTRA_POST_ID, postId)
            }
            startingActivity.startActivity(intent)
        }
    }

    @Inject
    lateinit var mViewModelFactory: ViewModelFactory

    private lateinit var mViewModel: PostsViewModel
    private lateinit var mPostId: String
    private var mPlayer: SimpleExoPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_details)
        setActivityTitle("")

        isDisplayHomeAsUpEnabled = true

        mPostId = intent.getStringExtra(EXTRA_POST_ID)

        mViewModel = ViewModelProviders.of(this, mViewModelFactory).get(PostsViewModel::class.java)
        setupObservers()

        mViewModel.loadPost(mPostId)
    }

    private fun setupObservers() {
        mViewModel.getPostObserver().observe(this, Observer { data ->
            data.run {
                showLoading(isLoading)

                response?.let {
                    Log.d("post", "->$it")
                    initPlayer(it.videoUrl)
                }

                error?.let {
                    Log.e("error", "->${it.message}")
                }
            }
        })
    }

    private fun showLoading(active: Boolean) {

    }

    private fun initPlayer(url: String) {
        mPlayer = ExoPlayerFactory.newSimpleInstance(this, DefaultRenderersFactory(this), DefaultTrackSelector())
        playerView.player = mPlayer

        mPlayer!!.prepare(buildMediaSource(Uri.parse(url)), true, false)
    }

    private fun buildMediaSource(uri: Uri): MediaSource {
        return ExtractorMediaSource.Factory(
            DefaultHttpDataSourceFactory("streamer")
        ).createMediaSource(uri)
    }

    private fun hideSystemUi() {
        playerView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LOW_PROFILE
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
    }

    private fun releasePlayer() {
      if (mPlayer != null) {
          mPlayer!!.release()
          mPlayer = null
      }
    }

    public override fun onPause() {
        super.onPause()
        if (Build.VERSION.SDK_INT <= 23) {
            releasePlayer()
        }
    }

    override fun onResume() {
        super.onResume()
        //hideSystemUi()
    }

    public override fun onStop() {
        super.onStop()
        if (Build.VERSION.SDK_INT > 23) {
            releasePlayer()
        }
    }
}
