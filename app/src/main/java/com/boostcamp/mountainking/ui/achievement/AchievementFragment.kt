package com.boostcamp.mountainking.ui.achievement

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.boostcamp.mountainking.R
import com.boostcamp.mountainking.databinding.FragmentAchievementBinding
import com.boostcamp.mountainking.entity.Achievement
import com.boostcamp.mountainking.entity.AchievementType
import com.google.android.material.tabs.TabLayout
import java.util.*

class AchievementFragment : Fragment() {

    private lateinit var achievementViewModel: AchievementViewModel
    private var _binding: FragmentAchievementBinding? = null

    private val binding get() = _binding!!
    var adapter = AchievementAdapter { achievement -> onClick(achievement) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        achievementViewModel = ViewModelProvider(this)[AchievementViewModel::class.java]
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
                        loadAchievementList()
                    }
                    getString(R.string.tl_achievement_category_complete) -> {
                        loadAchievementList(true)
                    }
                    getString(R.string.tl_achievement_category_incomplete) -> {
                        loadAchievementList(false)
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