package com.boostcamp.mountainking.ui.achievement

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.boostcamp.mountainking.databinding.FragmentAchievementBinding
import com.boostcamp.mountainking.entity.Achievement
import com.boostcamp.mountainking.entity.AchievementType
import java.util.*

class AchievementFragment : Fragment() {

    private lateinit var achievementViewModel: AchievementViewModel
    private var _binding: FragmentAchievementBinding? = null

    private val binding get() = _binding!!
    private lateinit var adapter: AchievementAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        achievementViewModel =
            ViewModelProvider(this).get(AchievementViewModel::class.java)

        _binding = FragmentAchievementBinding.inflate(inflater, container, false)

        adapter = AchievementAdapter { achievement -> onClick(achievement) }
        binding.rvAchievementList.adapter = adapter

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val achievement = Achievement(
            "1",
            "불암산 정복자",
            "불암산 등산",
            "",
            AchievementType.TRACKING_COUNT,
            1,
            2,
            false,
            Date(),
            30
        )

        adapter.submitList(listOf(achievement))
        Log.d("fragment", achievement.name)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onClick(achievement: Achievement) {

    }

}