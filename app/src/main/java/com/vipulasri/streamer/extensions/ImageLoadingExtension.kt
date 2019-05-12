package com.vipulasri.streamer.extensions

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.request.RequestOptions
import com.vipulasri.streamer.R

fun ImageView.loadImage(imageUrl: String, requestOptions: RequestOptions = RequestOptions.centerCropTransform().priority(Priority.HIGH)) {
    Glide.with(this)
            .load(imageUrl)
            .placeholder(R.drawable.placeholder_image)
            .apply(requestOptions)
            .into(this)
}

fun ImageView.loadImageUrl(url: String) {
    loadImage(url)
}