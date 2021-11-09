package com.boostcamp.mountainking.ui.tracking

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.boostcamp.mountainking.databinding.FragmentMountainSelectBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MountainSelectFragment : DialogFragment() {

    private var _binding: FragmentMountainSelectBinding? = null
    private val binding get() = _binding!!
    private val mountainSelectViewModel: MountainSelectViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMountainSelectBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = mountainSelectViewModel
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog.window?.setLayout(width, height)
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}