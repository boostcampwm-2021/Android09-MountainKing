package com.boostcamp.mountainking.ui.tracking.history.adapter

import androidx.recyclerview.widget.DiffUtil
import com.boostcamp.mountainking.entity.Tracking

object HistoryDiffUtilCallback : DiffUtil.ItemCallback<Tracking>() {
    override fun areItemsTheSame(oldItem: Tracking, newItem: Tracking): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Tracking, newItem: Tracking): Boolean {
        return oldItem == newItem
    }
}