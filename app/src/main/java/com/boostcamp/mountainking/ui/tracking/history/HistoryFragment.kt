package com.boostcamp.mountainking.ui.tracking.history

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.boostcamp.mountainking.R
import com.boostcamp.mountainking.data.LatLngAlt
import com.boostcamp.mountainking.databinding.FragmentHistoryBinding
import com.boostcamp.mountainking.entity.Tracking
import com.boostcamp.mountainking.ui.tracking.history.adapter.HistoryAdapter
import dagger.hilt.android.AndroidEntryPoint
import java.lang.IllegalStateException

@AndroidEntryPoint
class HistoryFragment
    : Fragment(), OnHistoryItemClickListener {
    private lateinit var binding: FragmentHistoryBinding
    private val historyViewModel: HistoryViewModel by viewModels()
    private val historyAdapter by lazy {
        HistoryAdapter(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        initView()
        historyViewModel.getTrackingList()
        historyViewModel.historyList.observe(viewLifecycleOwner) {
            historyAdapter.submitList(it)
        }
    }

    private fun initToolbar() {
        with(activity as AppCompatActivity) {
            setSupportActionBar(binding.tbHistory)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowTitleEnabled(false)
            supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_new_24)
        }

        binding.tbHistory.setNavigationOnClickListener {
            findNavController().navigate(R.id.action_history_to_tracking)
        }
    }

    private fun initView() {
        with(binding) {
            rvHistoryList.adapter = historyAdapter
        }
    }

    override fun onItemClick(altitudes: List<LatLngAlt>) {
        val action = HistoryFragmentDirections.actionHistoryToHistoryDetails(altitudes.toTypedArray())
        findNavController().navigate(action)
    }

    override fun onItemLongClick(tracking: Tracking): Boolean {
        activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setMessage(R.string.check_delete_tracking_history)
                .setPositiveButton(R.string.ok) { dialog, _ ->
                    historyViewModel.deleteTrackingItem(tracking)
                    dialog.dismiss()
                }
                .setNegativeButton(R.string.cancel) { dialog, _ ->
                    dialog.dismiss()
                }
            builder.create()
        }?.show() ?: throw IllegalStateException("Activity cannot be null")

        return true
    }

    companion object {
        private const val TAG = "HistoryFragment"
    }
}