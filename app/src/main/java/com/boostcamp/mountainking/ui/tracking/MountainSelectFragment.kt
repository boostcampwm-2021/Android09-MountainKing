package com.boostcamp.mountainking.ui.tracking

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.boostcamp.mountainking.databinding.FragmentMountainSelectBinding
import com.boostcamp.mountainking.entity.Mountain
import com.boostcamp.mountainking.util.EventObserver
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MountainSelectFragment : DialogFragment() {

    private var _binding: FragmentMountainSelectBinding? = null
    private val binding get() = _binding!!
    private val mountainSelectViewModel: MountainSelectViewModel by viewModels()
    private val mountainListAdapter = MountainListAdapter { mountain -> onMountainClicked(mountain)}

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
        setRecyclerView()
        mountainSelectViewModel.mountainNameList.observe(viewLifecycleOwner) {
            mountainListAdapter.submitList(it)
        }
        mountainSelectViewModel.dismiss.observe(viewLifecycleOwner, EventObserver{
            dismiss()
        })
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

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        val parentFragment = parentFragment
        if (parentFragment is DialogInterface.OnDismissListener) {
            parentFragment.onDismiss(dialog)
        }
    }

    private fun setRecyclerView() {
        binding.rvMountainList.adapter = mountainListAdapter
    }

    private fun onMountainClicked(mountain: Mountain) {
        mountainSelectViewModel.setTrackingMountainName(mountain.toString())
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}