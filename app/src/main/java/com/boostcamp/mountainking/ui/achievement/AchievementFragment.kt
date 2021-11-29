package com.boostcamp.mountainking.ui.achievement

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.boostcamp.mountainking.R
import com.boostcamp.mountainking.databinding.FragmentAchievementBinding
import com.boostcamp.mountainking.entity.Achievement
import com.boostcamp.mountainking.util.AchievementReceiver
import com.google.android.material.tabs.TabLayout
import com.kakao.kakaolink.v2.KakaoLinkResponse
import com.kakao.kakaolink.v2.KakaoLinkService
import com.kakao.message.template.ButtonObject
import com.kakao.message.template.ContentObject
import com.kakao.message.template.FeedTemplate
import com.kakao.message.template.LinkObject
import com.kakao.network.ErrorResult
import com.kakao.network.callback.ResponseCallback
import dagger.hilt.android.AndroidEntryPoint


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

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initObserve()
        initListener()
        achievementViewModel.loadAchievementList()
        binding.rvAchievementList.setOnTouchListener(object :
            OnSwipeTouchListener(requireContext()) {
            override fun onSwipeLeft() {
                Log.d("swipe", "left")
                binding.tlAchievementCategory.getTabAt(
                    kotlin.math.min(
                        binding.tlAchievementCategory.selectedTabPosition + 1,
                        binding.tlAchievementCategory.tabCount - 1
                    )
                )!!.select()
                Log.d("tabinfo_after", "$binding.tlAchievementCategory.selectedTabPosition ${binding.tlAchievementCategory.tabCount}")
            }

            override fun onSwipeRight() {
                Log.d("swipe", "right")
                binding.tlAchievementCategory.getTabAt(kotlin.math.max(binding.tlAchievementCategory.selectedTabPosition - 1, 0))!!.select()
            }

        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initView() {
        binding.rvAchievementList.adapter = adapter
        binding.tlAchievementCategory.selectTab(
            when (achievementViewModel.tabNameLiveData.value) {
                AchievementViewModel.TabName.TOTAL -> {
                    binding.tlAchievementCategory.getTabAt(0)
                }
                AchievementViewModel.TabName.COMPLETE -> {
                    binding.tlAchievementCategory.getTabAt(1)
                }
                AchievementViewModel.TabName.INCOMPLETE -> {
                    binding.tlAchievementCategory.getTabAt(2)
                }
                else -> {
                    binding.tlAchievementCategory.getTabAt(0)
                }
            }
        )
    }

    private fun initObserve() = with(achievementViewModel) {
        achievementListLiveData.observe(viewLifecycleOwner) {
            adapter.submitList(it)
            it.forEach { achievement ->
                Log.d("UpdateTest", "${achievement.name}: ${achievement.curProgress}")
            }
        }
        completedAchievementLiveData.observe(viewLifecycleOwner) {
            if (it != null) {
                onAchievementComplete(it.name)
            }
        }
        tabNameLiveData.observe(viewLifecycleOwner) {
            filterAchievementList()
        }

        totalAchievementCount.observe(viewLifecycleOwner) {
            binding.tvAchievementTotalCount.text = it.toString()
        }

        completeAchievementCount.observe(viewLifecycleOwner) {
            binding.tvAchievementCompleteCount.text = it.toString()
        }

        achievementScore.observe(viewLifecycleOwner) {
            binding.tvAchievementScore.text = it.toString()
        }

    }

    private fun initListener() = with(binding) {
        tlAchievementCategory.addOnTabSelectedListener(object :
            TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) = with(achievementViewModel) {
                when (tab?.text.toString()) {
                    getString(R.string.tl_achievement_category_total) -> {
                        setTabName(AchievementViewModel.TabName.TOTAL)
                    }
                    getString(R.string.tl_achievement_category_complete) -> {
                        setTabName(AchievementViewModel.TabName.COMPLETE)
                    }
                    getString(R.string.tl_achievement_category_incomplete) -> {
                        setTabName(AchievementViewModel.TabName.INCOMPLETE)
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
        sendKakao(achievement)
    }

    private fun sendKakao(achievement: Achievement) {

        val params = FeedTemplate.newBuilder(
            ContentObject.newBuilder(
                "${achievement.name} 달성",
                achievement.thumbnailUrl,
                LinkObject.newBuilder()
                    .setWebUrl("")
                    .setMobileWebUrl("")
                    .build()
            )
                .setDescrption("업적을 달성했습니다.")
                .build()
        ).build()

        KakaoLinkService.getInstance()
            .sendDefault(requireContext(), params, object : ResponseCallback<KakaoLinkResponse>() {
                override fun onFailure(errorResult: ErrorResult) {
                    Log.e("KAKAO_API", "카카오링크 공유 실패: $errorResult")
                    Toast.makeText(requireContext(), errorResult.toString(), Toast.LENGTH_SHORT)
                        .show()
                }

                override fun onSuccess(result: KakaoLinkResponse) {
                    Log.i("KAKAO_API", "카카오링크 공유 성공")

                    // 카카오링크 보내기에 성공했지만 아래 경고 메시지가 존재할 경우 일부 컨텐츠가 정상 동작하지 않을 수 있습니다.
                    Log.w("KAKAO_API", "warning messages: " + result.warningMsg)
                    Log.w("KAKAO_API", "argument messages: " + result.argumentMsg)
                }
            })

    }

    private fun onAchievementComplete(achievementName: String) {
        AchievementReceiver().notifyAchievementComplete(requireContext(), achievementName)
    }
}
