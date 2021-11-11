package com.boostcamp.mountainking.ui.tracking.history

import com.boostcamp.mountainking.data.LatLngAlt
import com.boostcamp.mountainking.entity.Tracking

interface OnHistoryItemClickListener {
    fun onItemClick(altitudes: List<LatLngAlt>)
    fun onItemLongClick(tracking: Tracking) : Boolean
}