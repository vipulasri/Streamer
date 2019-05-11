package com.vipulasri.streamer.extensions

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.request.RequestOptions

fun ImageView.loadImage(imageUrl: String, requestOptions: RequestOptions = RequestOptions.centerCropTransform().priority(Priority.HIGH)) {
    Glide.with(this)
            .load(imageUrl)
            .apply(requestOptions)
            .into(this)
}

fun ImageView.loadImageUrl(url: String) {
    loadImage(url)
}