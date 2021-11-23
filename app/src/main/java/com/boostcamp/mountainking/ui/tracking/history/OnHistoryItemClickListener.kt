package com.boostcamp.mountainking.ui.tracking.history

import com.boostcamp.mountainking.entity.Tracking

interface OnHistoryItemClickListener {
    fun onItemClick(tracking: Tracking)
    fun onItemLongClick(tracking: Tracking) : Boolean
    fun onDeleteClick(tracking: Tracking)
}