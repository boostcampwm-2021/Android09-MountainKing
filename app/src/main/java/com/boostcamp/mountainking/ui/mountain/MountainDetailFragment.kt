package com.boostcamp.mountainking.ui.mountain

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.boostcamp.mountainking.R
import com.boostcamp.mountainking.databinding.FragmentMountainDetailBinding
import com.boostcamp.mountainking.entity.Mountain
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MountainDetailFragment : Fragment() {

    private var _binding: FragmentMountainDetailBinding? = null
    private val mountainDetailViewModel: MountainDetailViewModel by viewModels()

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
        binding.mountain = Mountain(1, "", "", "", 1, "", "", "", "", "", "", "")
        initToolbar()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initToolbar() {
        with(activity as AppCompatActivity) {
            setSupportActionBar(binding.tbMountainDetailName)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowTitleEnabled(false)
            supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_new_24)
        }

        binding.tbMountainDetailName.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

    }

    private fun initObserve() = with(mountainDetailViewModel) {
        mountainLiveData.observe(viewLifecycleOwner) {
            binding.mountain = mountainDetailViewModel.mountainLiveData.value
        }
        mountainDetailViewModel.getMountain(mountainId)
    }

}