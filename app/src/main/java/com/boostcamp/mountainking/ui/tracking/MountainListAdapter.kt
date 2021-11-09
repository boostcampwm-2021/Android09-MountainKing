package com.boostcamp.mountainking.ui.tracking

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.boostcamp.mountainking.databinding.ItemMountainBinding
import com.boostcamp.mountainking.entity.Mountain

class MountainListAdapter : ListAdapter<Mountain, MountainListAdapter.MountainViewHolder>(object :
    DiffUtil.ItemCallback<Mountain>() {

    override fun areItemsTheSame(oldItem: Mountain, newItem: Mountain): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Mountain, newItem: Mountain): Boolean {
        return oldItem == newItem
    }
}) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MountainViewHolder {
        return MountainViewHolder(
            ItemMountainBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MountainViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class MountainViewHolder(private val binding: ItemMountainBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(mountain: Mountain) {
            binding.mountain = mountain
        }

        fun freeImage() {
            //TODO: Free glide image
        }
    }
}