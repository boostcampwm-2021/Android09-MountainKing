package com.boostcamp.mountainking.util

import android.util.Log
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.BindingAdapter
import com.boostcamp.mountainking.R
import com.boostcamp.mountainking.data.LatLngAlt
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

@BindingAdapter("locationList")
fun setImageFromLocationList(view: ImageView, list: List<LatLngAlt>) {
    val url = getURL(list)
    Log.d("url", url)
    Glide.with(view.context)
        .load(url.replace("http://", "https://"))
        .centerCrop()
        .placeholder(R.drawable.ic_baseline_terrain_24)
        .into(view)
}

@BindingAdapter("tracking_distance")
fun setDistance(view: AppCompatTextView, trackingDistance: Int?) {
    if (trackingDistance != null) {
        val distanceString = "$trackingDistance m"
        view.text = distanceString
    }
}