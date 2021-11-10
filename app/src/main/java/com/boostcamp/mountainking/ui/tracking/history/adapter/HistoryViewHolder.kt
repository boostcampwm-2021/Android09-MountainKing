package com.boostcamp.mountainking.ui.tracking.history.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.boostcamp.mountainking.databinding.ItemTrackingHistoryBinding
import com.boostcamp.mountainking.entity.Tracking
import com.boostcamp.mountainking.ui.tracking.history.OnHistoryItemClickListener

class HistoryViewHolder(
    val binding: ItemTrackingHistoryBinding,
    private val onItemClickListener: OnHistoryItemClickListener
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(tracking: Tracking) {
        binding.tracking = tracking
        itemView.setOnClickListener { onItemClickListener.onItemClick() }
    }

    companion object{
        fun from(parent : ViewGroup,
                 onHistoryItemClickListener: OnHistoryItemClickListener) : HistoryViewHolder {
            return HistoryViewHolder(
                ItemTrackingHistoryBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ),
                onHistoryItemClickListener
            )
        }
    }
}