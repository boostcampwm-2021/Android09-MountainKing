package com.boostcamp.mountainking.ui.tracking.history.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.boostcamp.mountainking.entity.Tracking
import com.boostcamp.mountainking.ui.tracking.history.OnHistoryItemClickListener

class HistoryAdapter(
    private val onItemClickListener : OnHistoryItemClickListener
) : ListAdapter<Tracking, HistoryViewHolder>(HistoryDiffUtilCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        return HistoryViewHolder.from(parent, onItemClickListener)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onViewRecycled(holder: HistoryViewHolder) {
        super.onViewRecycled(holder)
        holder.freeImage()
    }
}