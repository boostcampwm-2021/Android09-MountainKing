package com.boostcamp.mountainking.ui.achievement

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.boostcamp.mountainking.R
import com.boostcamp.mountainking.databinding.FragmentAchievementBinding
import com.boostcamp.mountainking.entity.Achievement
import com.boostcamp.mountainking.entity.AchievementType
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class AchievementFragment : Fragment() {

    private val achievementViewModel: AchievementViewModel by viewModels()
    private var _binding: FragmentAchievementBinding? = null

    private val binding get() = _binding!!
    var adapter = AchievementAdapter { achievement -> onClick(achievement) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAchievementBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initObserve()
        initListener()
    }

    override fun onResume() {
        super.onResume()
        achievementViewModel.loadAchievementList()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initView() {
        binding.rvAchievementList.adapter = adapter
    }

    private fun initObserve() {
        achievementViewModel.achievementListLiveData.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

    private fun initListener() = with(binding) {
        tlAchievementCategory.addOnTabSelectedListener(object :
            TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) = with(achievementViewModel) {
                when (tab?.text.toString()) {
                    getString(R.string.tl_achievement_category_total) -> {
                        filterAchievementList()
                    }
                    getString(R.string.tl_achievement_category_complete) -> {
                        filterAchievementList(true)
                    }
                    getString(R.string.tl_achievement_category_incomplete) -> {
                        filterAchievementList(false)
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
    }

    private fun onClick(achievement: Achievement) {
        Log.d("onClick", achievement.name)
        //TODO: 공유하기
    }
}