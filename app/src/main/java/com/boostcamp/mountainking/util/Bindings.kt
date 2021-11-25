package com.boostcamp.mountainking.util

import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.ColorFilter
import android.graphics.PorterDuff
import android.os.Build
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.boostcamp.mountainking.R
import com.boostcamp.mountainking.data.LatLngAlt
import com.bumptech.glide.Glide

@BindingAdapter("imageURL")
fun setImageFromURL(view: ImageView, url: String) {
    Log.d("imageURL", url)
    val circularProgressDrawable = CircularProgressDrawable(view.context)
    circularProgressDrawable.strokeWidth = 5f
    circularProgressDrawable.centerRadius = 30f
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        circularProgressDrawable.colorFilter = BlendModeColorFilter(
            ContextCompat.getColor(view.context, R.color.mountain_blue),
            BlendMode.SRC_ATOP
        )
    } else {
        circularProgressDrawable.setColorFilter(
            ContextCompat.getColor(
                view.context,
                R.color.mountain_blue
            ), PorterDuff.Mode.SRC_ATOP
        )
    }
    circularProgressDrawable.start()
    Glide.with(view.context)
        .load(url.replace("http://", "https://"))
        .centerCrop()
        .placeholder(circularProgressDrawable)
        .error(R.drawable.ic_baseline_terrain_24)
        .into(view)
}

@BindingAdapter("locationList")
fun setImageFromLocationList(view: ImageView, list: List<LatLngAlt>) {
    val url = getURL(list)
    Log.d("url", url)
    val circularProgressDrawable = CircularProgressDrawable(view.context)
    circularProgressDrawable.strokeWidth = 5f
    circularProgressDrawable.centerRadius = 30f
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        circularProgressDrawable.colorFilter = BlendModeColorFilter(
            ContextCompat.getColor(view.context, R.color.mountain_blue),
            BlendMode.SRC_ATOP
        )
    } else {
        circularProgressDrawable.setColorFilter(
            ContextCompat.getColor(
                view.context,
                R.color.mountain_blue
            ), PorterDuff.Mode.SRC_ATOP
        )
    }
    circularProgressDrawable.start()
    Glide.with(view.context)
        .load(url.replace("http://", "https://"))
        .centerCrop()
        .placeholder(circularProgressDrawable)
        .error(R.drawable.ic_baseline_terrain_24)
        .into(view)
}

@BindingAdapter("tracking_distance")
fun setDistance(view: AppCompatTextView, trackingDistance: Int?) {
    if (trackingDistance != null && trackingDistance >= 0) {
        val distanceString = "$trackingDistance m"
        view.text = distanceString
    } else {
        view.text = null
    }
}

@BindingAdapter("tracking_step")
fun setStep(view: TextView, trackingStep: Int?) {
    if (trackingStep != null && trackingStep >= 0) {
        val stepString = "$trackingStep 걸음"
        view.text = stepString
    } else {
        view.text = null
    }
}