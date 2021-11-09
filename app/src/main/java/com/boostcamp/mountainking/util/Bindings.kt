package com.boostcamp.mountainking.util

import android.util.Log
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.boostcamp.mountainking.R
import com.bumptech.glide.Glide

@BindingAdapter("imageURL")
fun setImageFromURL(view: ImageView, url: String) {
    Log.d("imageURL", url)
    Glide.with(view.context)
        .load(url.replace("http://", "https://"))
        .centerCrop()
        .placeholder(R.drawable.ic_baseline_terrain_24)
        .into(view)
}