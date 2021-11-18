package com.boostcamp.mountainking.ui.mountain

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.boostcamp.mountainking.databinding.ItemWeatherBinding
import com.boostcamp.mountainking.entity.Daily

class WeatherListAdapter :
    ListAdapter<Daily, WeatherListAdapter.DailyViewHolder>(DailyDiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyViewHolder {

        val binding =
            ItemWeatherBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return DailyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DailyViewHolder, position: Int) {
        val daily = getItem(position)
        holder.bind(daily)
    }

    class DailyViewHolder(
        private val binding: ItemWeatherBinding,
    ) :
        RecyclerView.ViewHolder(binding.root) {

        private var currentWeather: Daily? = null

        fun bind(daily: Daily) {
            currentWeather = daily

            binding.daily = daily
            binding.executePendingBindings()
        }
    }
}


object DailyDiffCallback : DiffUtil.ItemCallback<Daily>() {
    override fun areItemsTheSame(oldItem: Daily, newItem: Daily): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Daily, newItem: Daily): Boolean {
        return oldItem.dt == newItem.dt
    }

}