package com.boostcamp.mountainking.ui.mountain

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.boostcamp.mountainking.databinding.FragmentMountainDetailBinding
import com.boostcamp.mountainking.entity.Mountain
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MountainDetailFragment : Fragment() {

    private var _binding: FragmentMountainDetailBinding? = null
    private val mountainDetailViewModel: MountainDetailViewModel by viewModels()
    private val weatherListAdapter = WeatherListAdapter()
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var mountainId: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentMountainDetailBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mountainId = arguments?.getInt("mountainId") ?: 1
        initObserve()
        loadData()
        binding.mountain = Mountain(1, "", "", "", 1, "", "", "", "", "", "", "", 0.0, 0.0)
        binding.mtbMountainDetail.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        binding.rvMountainDetailWeather.adapter = weatherListAdapter
    }

    private fun loadData() = with(mountainDetailViewModel) {
        loadMountain(mountainId)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initObserve() = with(mountainDetailViewModel) {
        mountainLiveData.observe(viewLifecycleOwner) {
            binding.mountain = mountainDetailViewModel.mountainLiveData.value
            if(it?.mountainDetails == null) {
                binding.tvMountainDetailLabel.visibility = View.GONE
            }
            if(it?.tourDetails == null) {
                binding.tvMountainDetailTourLabel.visibility = View.GONE
            }
            if(it?.transportationDetails == null) {
                binding.tvMountainDetailTransportationLabel.visibility = View.GONE
            }
        }
        weatherLiveData.observe(viewLifecycleOwner) {
            weatherListAdapter.submitList(it.daily)
        }
    }

}