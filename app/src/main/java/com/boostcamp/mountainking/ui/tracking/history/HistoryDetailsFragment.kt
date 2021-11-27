package com.boostcamp.mountainking.ui.tracking.history

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.boostcamp.mountainking.R
import com.boostcamp.mountainking.databinding.FragmentHistoryDetailsBinding
import com.boostcamp.mountainking.ui.tracking.LocationService
import com.boostcamp.mountainking.ui.tracking.history.valueformatter.XAxisLabelFormatter
import com.boostcamp.mountainking.ui.tracking.history.valueformatter.YAxisLabelFormatter
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.naver.maps.geometry.LatLng
import com.naver.maps.geometry.LatLngBounds
import com.naver.maps.map.*
import com.naver.maps.map.overlay.Align
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.PathOverlay
import com.naver.maps.map.util.MarkerIcons


class HistoryDetailsFragment : Fragment(), OnMapReadyCallback {
    private lateinit var binding: FragmentHistoryDetailsBinding
    private var mapView: MapView? = null
    private var naverMap: NaverMap? = null
    private val args: HistoryDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHistoryDetailsBinding.inflate(inflater, container, false)

        mapView = binding.mvNaver
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapView?.onCreate(savedInstanceState)
        mapView?.getMapAsync(this)
        initView()
        setListener()
    }

    private fun initView() {
        initToolbar()
        initAltitudeGraph()
        initMountainName()
        binding.tracking = args.tracking
    }

    private fun initMountainName() {
        binding.tvHistoryDetailsToolbarTitle.text = args.tracking.mountainName
    }

    private fun initToolbar() {
        with(activity as AppCompatActivity) {
            setSupportActionBar(binding.tbHistoryDetails)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowTitleEnabled(false)
            supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_new_24)
        }
        binding.tbHistoryDetails.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setListener() {
        with(binding) {
            btnTypeNormal.setOnClickListener { onRadioClicked(it) }
            btnTypeSatellite.setOnClickListener { onRadioClicked(it) }
            btnTypeHybrid.setOnClickListener { onRadioClicked(it) }
        }
    }

    private fun onRadioClicked(view: View) {
        if(view is RadioButton) {
            val checked = view.isChecked

            when(view.id) {
                R.id.btn_type_normal -> {
                    if(checked) {
                        naverMap?.mapType = NaverMap.MapType.Basic
                    }
                }
                R.id.btn_type_satellite -> {
                    if(checked) {
                        naverMap?.mapType = NaverMap.MapType.Satellite
                    }
                }
                R.id.btn_type_hybrid -> {
                    if(checked) {
                        naverMap?.mapType = NaverMap.MapType.Hybrid
                    }
                }
            }
        }
    }

    override fun onMapReady(naverMap: NaverMap) {
        val uiSettings = naverMap.uiSettings
        with(uiSettings) {
            logoGravity = Gravity.START
            isLogoClickEnabled = true
        }

        // Path initialize
        if (args.tracking.coordinates.size >= 2) {
            val path = PathOverlay()
            with(path) {
                coords = args.tracking.coordinates.map { LatLng(it.latitude, it.longitude) }
                width = MAP_PATH_WIDTH
                outlineWidth = MAP_PATH_OUTLINE_WIDTH
                color = this@HistoryDetailsFragment.requireContext().getColor(R.color.blue)
                map = naverMap
                val cameraUpdate =
                    CameraUpdate.fitBounds(
                        LatLngBounds.Builder().include(coords).build(),
                        MAP_PADDING
                    )
                        .animate(CameraAnimation.Fly, FLY_DURATION)
                naverMap.moveCamera(cameraUpdate)
            }
        } else if (args.tracking.coordinates.isNotEmpty()) {
            val cameraPosition = CameraPosition(
                LatLng(
                    args.tracking.coordinates[0].latitude,
                    args.tracking.coordinates[0].longitude
                ), MAP_ZOOM_LEVEL
            )
            naverMap.cameraPosition = cameraPosition
        }

        // Marker initialize
        val startMarker = Marker()
        val startPosition = args.tracking.coordinates[0]
        with(startMarker) {
            position = LatLng(startPosition.latitude, startPosition.longitude)
            icon = MarkerIcons.RED
            width = Marker.SIZE_AUTO
            height = Marker.SIZE_AUTO
            captionText = "시작지점"
            captionColor = Color.RED
            setCaptionAligns(Align.Top)

            map = naverMap
        }

        val endMarker = Marker()
        val endPosition = args.tracking.coordinates.last()
        with(endMarker) {
            position = LatLng(endPosition.latitude, endPosition.longitude)
            icon = MarkerIcons.RED
            width = Marker.SIZE_AUTO
            height = Marker.SIZE_AUTO
            captionText = "도착지점"
            captionColor = Color.RED
            setCaptionAligns(Align.Top)

            map = naverMap
        }

        this.naverMap = naverMap
    }

    private fun initAltitudeGraph() {
        if (args.tracking.coordinates.isNotEmpty()) {
            val entries = mutableListOf<Entry>().apply {
                addAll(args.tracking.coordinates.map {
                    BarEntry(
                        LocationService.FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS.toFloat()
                                * (args.tracking.coordinates.indexOf(it) + 1) / 1000,
                        String.format("%.2f", it.altitude).toFloat()
                    )
                })
            }

            val set = LineDataSet(entries, "Altitude").apply {
                color = ContextCompat.getColor(requireActivity(), R.color.thick_green)
                lineWidth = 5f
                circleRadius = 1.0f
                setCircleColor(ContextCompat.getColor(requireActivity(), R.color.white))
                setDrawValues(false)
            }

            val dataSet: MutableList<ILineDataSet> = mutableListOf<ILineDataSet>().apply {
                add(set)
            }
            val data = LineData(dataSet)

            with(binding.loBottomSheet) {
                lcAltitudeGraph.run {
                    axisLeft.run {
                        axisMaximum =
                            args.tracking.coordinates.maxOf { it.altitude }.toFloat() + 10f
                        axisMinimum = args.tracking.coordinates.minOf { it.altitude }.toFloat()
                        granularity = 10.0f
                        setDrawLabels(true)
                        setDrawAxisLine(true)
                        valueFormatter = YAxisLabelFormatter()
                        description.text = "단위 m"
                    }
                    xAxis.run {
                        position = XAxis.XAxisPosition.BOTTOM
                        granularity = 1.0f
                        valueFormatter = XAxisLabelFormatter()
                        description.text = "단위 sec"
                    }
                    axisRight.isEnabled = false
                    this.data = data
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        mapView?.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView?.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView?.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView?.onSaveInstanceState(outState)
    }

    override fun onStop() {
        super.onStop()
        mapView?.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView?.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView?.onLowMemory()
    }

    companion object {
        private const val FLY_DURATION = 2000L
        private const val MAP_PADDING = 200
        private const val MAP_PATH_WIDTH = 10
        private const val MAP_PATH_OUTLINE_WIDTH = 0
        private const val MAP_ZOOM_LEVEL = 14.0
    }
}