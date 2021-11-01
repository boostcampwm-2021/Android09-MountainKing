package com.boostcamp.mountainking.ui.achievement

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.boostcamp.mountainking.databinding.ItemAchievementBinding
import com.boostcamp.mountainking.entity.Achievement

class AchievementAdapter(private val onClick: (Achievement) -> Unit) :
    ListAdapter<Achievement, AchievementAdapter.AchievementViewHolder>(AchievementDiffCallback) {
    private val items = mutableListOf<Achievement>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AchievementViewHolder {

        val binding =
            ItemAchievementBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return AchievementViewHolder(binding, onClick)
    }

    override fun onBindViewHolder(holder: AchievementViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

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
            binding.achievement = achievement
            binding.executePendingBindings()
            Log.d("bindtest", achievement.name)
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