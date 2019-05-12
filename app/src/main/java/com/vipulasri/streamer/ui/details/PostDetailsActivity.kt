package com.vipulasri.streamer.ui.details

import android.content.Context
import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.vipulasri.streamer.R
import com.vipulasri.streamer.inject.ViewModelFactory
import com.vipulasri.streamer.ui.PostsViewModel
import com.vipulasri.streamer.ui.base.BaseActivity
import javax.inject.Inject
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import kotlinx.android.synthetic.main.activity_post_details.*
import com.google.android.exoplayer2.source.MediaSource
import android.net.Uri
import android.view.View
import android.os.Build
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.vipulasri.streamer.extensions.fetchErrorMessage
import com.vipulasri.streamer.extensions.setGone
import com.vipulasri.streamer.extensions.setVisible
import com.vipulasri.streamer.extensions.whenNotNull
import com.vipulasri.streamer.utils.StatusBarUtil
import kotlinx.android.synthetic.main.layout_error_light.*
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.vipulasri.streamer.model.PostDetails


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
    private var mPost: PostDetails? = null
    private var mPlayer: SimpleExoPlayer? = null
    private var mPlaybackPosition = 0L
    private var mCurrentWindow = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_details)

        setActivityTitle("")
        isDisplayHomeAsUpEnabled = true

        whenNotNull(toolbar){
            it.setNavigationIcon(R.drawable.ic_close_black_24dp)
            it.navigationIcon!!.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_IN)
        }

        StatusBarUtil.setColor(this, ContextCompat.getColor(this, R.color.black))

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
                    mPost = it
                    initPlayer()
                }

                error?.let {
                    //Log.e("error", "->${it.message}")
                    showError(fetchErrorMessage(it))
                }
            }
        })
    }

    private fun showLoading(active: Boolean) {
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

    private fun showError(message: String) {
        showContent(false)
        view_error.setVisible()
        text_error_message.text = message
        button_error_retry.setOnClickListener { mViewModel.loadPost(mPostId) }
    }

    private fun showContent(active: Boolean){
        if(active) {
            playerView.setVisible()
        } else {
            playerView.setGone()
        }
    }

    private fun initPlayer() {
        mPlayer = ExoPlayerFactory.newSimpleInstance(this, DefaultRenderersFactory(this), DefaultTrackSelector()).apply {
            seekTo(mCurrentWindow, mPlaybackPosition)
            playWhenReady = true
            prepare(buildMediaSource(Uri.parse(mPost!!.videoUrl)), true, false)
            addListener(object: Player.DefaultEventListener(){
                override fun onPlayerError(error: ExoPlaybackException?) {
                    super.onPlayerError(error)
                    val message = if(error != null) error.message else getString(R.string.error_msg_unknown)
                    Toast.makeText(this@PostDetailsActivity, message, Toast.LENGTH_SHORT).show()
                    finish()
                }

            })
        }

        playerView.player = mPlayer
    }

    private fun buildMediaSource(uri: Uri): MediaSource {
        val dataSourceFactory = DefaultDataSourceFactory(this, "streamer")
        return ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(uri)
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
        if ((Build.VERSION.SDK_INT <= 23 || mPlayer == null) && mPost != null) {
            initPlayer()
        }
    }

    public override fun onStop() {
        super.onStop()
        if (Build.VERSION.SDK_INT > 23) {
            releasePlayer()
        }
    }
}
