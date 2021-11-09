package com.boostcamp.mountainking.ui.tracking

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.boostcamp.mountainking.databinding.ItemMountainBinding
import com.boostcamp.mountainking.entity.Mountain
import com.bumptech.glide.Glide

class MountainListAdapter(private val onClick: (Mountain) -> Unit) :
    ListAdapter<Mountain, MountainListAdapter.MountainViewHolder>(object :
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
            ),
            onClick
        )
    }

    override fun onViewRecycled(holder: MountainViewHolder) {
        super.onViewRecycled(holder)
        holder.freeImage()
    }

    override fun onBindViewHolder(holder: MountainViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class MountainViewHolder(
        private val binding: ItemMountainBinding,
        private val onClick: (Mountain) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {

        lateinit var mountain: Mountain

        init {
            itemView.setOnClickListener {
                onClick(mountain)
            }
        }

        fun bind(mountain: Mountain) {
            binding.mountain = mountain
            this.mountain = mountain
        }

        fun freeImage() {
            Glide.with(itemView.context).clear(binding.ivMountain)
        }
    }
}