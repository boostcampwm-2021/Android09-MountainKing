package com.boostcamp.mountainking.ui.achievement

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.boostcamp.mountainking.databinding.FragmentAchievementBinding

class AchievementFragment : Fragment() {

    private lateinit var achievementViewModel: AchievementViewModel
    private var _binding: FragmentAchievementBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        achievementViewModel =
            ViewModelProvider(this).get(AchievementViewModel::class.java)

        _binding = FragmentAchievementBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}