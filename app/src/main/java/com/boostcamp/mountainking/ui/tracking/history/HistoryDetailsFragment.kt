package com.boostcamp.mountainking.ui.tracking.history

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.boostcamp.mountainking.R
import com.boostcamp.mountainking.databinding.FragmentHistoryDetailsBinding
import com.boostcamp.mountainking.ui.tracking.LocationService
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet


class HistoryDetailsFragment : Fragment() {
    private lateinit var binding: FragmentHistoryDetailsBinding
    private val args: HistoryDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHistoryDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        initView()
    }

    private fun initView() {
        val entries = mutableListOf<Entry>().apply {
            addAll(args.altitudeList.map { BarEntry(
                LocationService.FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS.toFloat()
                        * (args.altitudeList.indexOf(it) + 1),
                String.format("%.2f", it.altitude).toFloat()
            ) })
        }

        val set = LineDataSet(entries, "Altitude").apply {
            color = ContextCompat.getColor(requireActivity(), R.color.light_green)
            setCircleColor(ContextCompat.getColor(requireActivity(), R.color.thick_green))
            setDrawValues(false)
        }

        val dataSet: MutableList<ILineDataSet> = mutableListOf<ILineDataSet>().apply {
            add(set)
        }
        val data = LineData(dataSet)


        with(binding) {
            lcHistoryAltitude.run {
                axisLeft.run {
                    axisMaximum = args.altitudeList.maxOf { it.altitude }.toFloat() + 10f
                    axisMinimum = args.altitudeList.minOf { it.altitude }.toFloat()
                    granularity = 10.0f
                    setDrawLabels(true)
                    setDrawAxisLine(true)
                }
                xAxis.run {
                    position = XAxis.XAxisPosition.BOTTOM
                    granularity = 1.0f

                }
                axisRight.isEnabled = false
                animateY(1000)

                this.data = data
            }
        }
    }
    private fun initToolbar() {
        with(activity as AppCompatActivity) {
            setSupportActionBar(binding.tbHistoryDetails)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowTitleEnabled(false)
            supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_new_24)
        }
        binding.tbHistoryDetails.setNavigationOnClickListener {
            findNavController().navigate(R.id.action_historyDetails_to_history)
        }
    }
}