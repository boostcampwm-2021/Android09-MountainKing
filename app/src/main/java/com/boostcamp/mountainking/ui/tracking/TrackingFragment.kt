package com.boostcamp.mountainking.ui.tracking


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.boostcamp.mountainking.databinding.FragmentTrackingBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TrackingFragment : Fragment() {
    private val trackingViewModel: TrackingViewModel by viewModels()
    private var _binding: FragmentTrackingBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTrackingBinding.inflate(inflater, container, false)
        binding.trackingViewModel = trackingViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListener()
    }

    private fun setListener() {
        with(binding) {
            btnTrackingStart.setOnClickListener {
                startLocationService()
            }
        }
    }

    private fun startLocationService() {
        trackingViewModel.startService()
    }

    override fun onStart() {
        super.onStart()
        trackingViewModel.bindService()
    }

    override fun onStop() {
        super.onStop()
        trackingViewModel.unbindService()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}