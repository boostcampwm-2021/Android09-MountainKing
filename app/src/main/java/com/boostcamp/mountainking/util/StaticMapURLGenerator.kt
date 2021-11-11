package com.boostcamp.mountainking.util

import com.boostcamp.mountainking.STATIC_MAP_KEY
import com.boostcamp.mountainking.data.LatLngAlt

fun getURL(list: List<LatLngAlt>): String {
    val baseURL = "http://api.vworld.kr/req/image?service=image&request=getmap&key=$STATIC_MAP_KEY"
    val centerLatitude = (list.maxOf { it.latitude } + list.minOf { it.latitude }) / 2
    val centerLongitude = (list.maxOf { it.longitude } + list.minOf { it.longitude }) / 2
    val centerString = "center=$centerLongitude,$centerLatitude"
    val zoom = "zoom=15"
    val size = "size=800,800"
    val route =
        "route=point:" + list.joinToString(",") { "${it.longitude} ${it.latitude}" } + "|width:3|color:blue"
    return listOf(baseURL, centerString, zoom, size, route).joinToString("&")
}