package com.boostcamp.mountainking.util

import android.util.Log
import com.boostcamp.mountainking.STATIC_MAP_KEY
import com.boostcamp.mountainking.data.LatLngAlt

fun getURL(list: List<LatLngAlt>): String {
    return if (list.isNotEmpty()) {
        val baseURL =
            "http://api.vworld.kr/req/image?service=image&request=getmap&key=$STATIC_MAP_KEY"
        val centerLatitude = (list.maxOf { it.latitude } + list.minOf { it.latitude }) / 2
        val centerLongitude = (list.maxOf { it.longitude } + list.minOf { it.longitude }) / 2
        val centerString = "center=$centerLongitude,$centerLatitude"
        val zoom = "zoom=15"
        val size = "size=800,800"
        val route =
            "route=point:" + filterList(list) + "|width:3|color:blue"
        listOf(baseURL, centerString, zoom, size, route).joinToString("&")
    } else {
        ""
    }
}

fun filterList(list: List<LatLngAlt>): String {
    Log.d("filter", list.toString())
    if (list.size > 250) {
        val n = list.size / 250
        val filtered = list.filterIndexed { index, latLngAlt -> index % n == 0 }
        Log.d("filter", filtered.toString())
        return filtered.joinToString(",") { "${it.longitude} ${it.latitude}" }
    }

    return list.joinToString(",") { "${it.longitude} ${it.latitude}" }
}