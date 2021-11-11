package com.boostcamp.mountainking.ui.tracking.history

import com.boostcamp.mountainking.entity.Tracking

interface OnHistoryItemClickListener {
    fun onItemClick()
    fun onItemLongClick(tracking: Tracking) : Boolean
}