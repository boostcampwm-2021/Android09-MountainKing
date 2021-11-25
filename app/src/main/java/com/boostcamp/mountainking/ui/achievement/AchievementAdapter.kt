package com.boostcamp.mountainking.ui.achievement

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.boostcamp.mountainking.databinding.ItemAchievementBinding
import com.boostcamp.mountainking.entity.Achievement
import com.bumptech.glide.Glide

class AchievementAdapter(private val onClick: (Achievement) -> Unit) :
    ListAdapter<Achievement, AchievementAdapter.AchievementViewHolder>(AchievementDiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AchievementViewHolder {

        val binding =
            ItemAchievementBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return AchievementViewHolder(binding, onClick)
    }

    override fun onBindViewHolder(holder: AchievementViewHolder, position: Int) {
        val achievement = getItem(position)
        holder.bind(achievement)
    }

    override fun onViewRecycled(holder: AchievementViewHolder) {
        super.onViewRecycled(holder)
        holder.freeImage()
    }

    class AchievementViewHolder(
        private val binding: ItemAchievementBinding,
        val onClick: (Achievement) -> Unit,
    ) :
        RecyclerView.ViewHolder(binding.root) {

        private val share = binding.ivAchievementItemShare
        private var currentAchievement: Achievement? = null

        init {
            share.setOnClickListener {
                currentAchievement?.let {
                    onClick(it)
                }
            }
        }

        fun bind(achievement: Achievement) {
            currentAchievement = achievement

            binding.achievement = achievement
            binding.executePendingBindings()
            binding.root.setBackgroundColor(
                if (achievement.isComplete) {
                    Color.WHITE
                } else {
                    Color.LTGRAY
                }
            )
            Log.d("bindtest", achievement.name)
        }

        fun freeImage() {
            Glide.with(itemView.context).clear(binding.ivAchievementItemShare)
            Glide.with(itemView.context).clear(binding.ivAchievementItemThumbnail)
        }

    }

}


object AchievementDiffCallback : DiffUtil.ItemCallback<Achievement>() {
    override fun areItemsTheSame(oldItem: Achievement, newItem: Achievement): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Achievement, newItem: Achievement): Boolean {
        return oldItem.id == newItem.id
    }

}