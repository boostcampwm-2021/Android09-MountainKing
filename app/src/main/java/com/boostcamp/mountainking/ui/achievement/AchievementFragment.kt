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

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        achievementViewModel =
            ViewModelProvider(this).get(AchievementViewModel::class.java)

        _binding = FragmentAchievementBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textDashboard
        achievementViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}