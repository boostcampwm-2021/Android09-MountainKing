package com.boostcamp.mountainking.ui.tracking.history

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
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
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.PathOverlay


class HistoryDetailsFragment : Fragment(), OnMapReadyCallback {
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
        binding.mvNaver.onCreate(savedInstanceState)
        binding.mvNaver.getMapAsync(this)
        initView()
    }

    private fun initView() {
        initToolbar()
        initAltitudeGraph()
        initMountainName()
        setOnTouchListener()
    }

    private fun initMountainName() {
        binding.tvHistoryDetailsToolbarTitle.text = args.mountainName
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

    private fun initAltitudeGraph() {
        if (args.altitudeList.isNotEmpty()) {
            val entries = mutableListOf<Entry>().apply {
                addAll(args.altitudeList.map {
                    BarEntry(
                        LocationService.FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS.toFloat()
                                * (args.altitudeList.indexOf(it) + 1) / 1000,
                        String.format("%.2f", it.altitude).toFloat()
                    )
                })
            }

            val set = LineDataSet(entries, "Altitude").apply {
                color = ContextCompat.getColor(requireActivity(), R.color.light_green)
                lineWidth = 5f
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
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setOnTouchListener() {
        binding.mvNaver.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    binding.svHistoryDetails.requestDisallowInterceptTouchEvent(true)
                    true
                }
                MotionEvent.ACTION_UP -> {
                    binding.svHistoryDetails.requestDisallowInterceptTouchEvent(false)
                    false
                }
                else -> {
                    false
                }
            }
        }

    }

    override fun onMapReady(naverMap: NaverMap) {
        if (args.altitudeList.size >= 2) {
            val path = PathOverlay()
            with(path) {
                coords = args.altitudeList.map { LatLng(it.latitude, it.longitude) }
                width = 10
                outlineWidth = 0
                color = this@HistoryDetailsFragment.requireContext().getColor(R.color.blue)
                map = naverMap
                val cameraPosition = CameraPosition(
                    path.coords[path.coords.size / 2],
                    16.0
                )
                naverMap.cameraPosition = cameraPosition
            }
        } else if (args.altitudeList.isNotEmpty()) {
            val cameraPosition = CameraPosition(
                LatLng(
                    args.altitudeList[0].latitude,
                    args.altitudeList[0].longitude
                ), 16.0
            )
            naverMap.cameraPosition = cameraPosition
        }
    }

    override fun onStart() {
        super.onStart()
        binding.mvNaver.onStart()
    }

    override fun onResume() {
        super.onResume()
        binding.mvNaver.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.mvNaver.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.mvNaver.onSaveInstanceState(outState)
    }

    override fun onStop() {
        super.onStop()
        binding.mvNaver.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.mvNaver.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mvNaver.onLowMemory()
    }
}