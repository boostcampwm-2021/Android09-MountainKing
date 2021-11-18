package com.boostcamp.mountainking.ui.tracking


import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.boostcamp.mountainking.BuildConfig
import com.boostcamp.mountainking.R
import com.boostcamp.mountainking.databinding.FragmentTrackingBinding
import com.boostcamp.mountainking.util.AchievementReceiver
import com.boostcamp.mountainking.util.EventObserver
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import com.naver.maps.geometry.LatLng
import com.naver.maps.geometry.LatLngBounds
import com.naver.maps.map.*
import com.naver.maps.map.overlay.PathOverlay
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TrackingFragment : Fragment(), DialogInterface.OnDismissListener, OnMapReadyCallback {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var mapView: MapView
    private val trackingViewModel: TrackingViewModel by viewModels()

    private var _binding: FragmentTrackingBinding? = null
    private val binding get() = _binding!!
    private var locationCoords = listOf<LatLng>()
    private var naverMap: NaverMap? = null
    private val path = PathOverlay()
    private var isTracking = false

    private val requestPermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { isGranted ->
            if (isGranted.values.all { it }) {
                if (!isTracking) {
                    getLastLocation()
                }
                else {
                    trackingViewModel.startService()
                    isTracking = true
                }
            } else {
                Snackbar.make(
                    binding.root,
                    R.string.permission_rationale,
                    Snackbar.LENGTH_INDEFINITE
                )
                    .setAction(
                        R.string.settings
                    ) {
                        val intent = Intent()
                        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                        val uri = Uri.fromParts(
                            "package",
                            BuildConfig.APPLICATION_ID, null
                        )
                        intent.data = uri
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                    }
                    .show()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTrackingBinding.inflate(inflater, container, false)
        mapView = binding.mvNaver
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.trackingViewModel = trackingViewModel
        with(binding) {
            lifecycleOwner = viewLifecycleOwner
            btnTrackingHistory.setOnClickListener {
                findNavController().navigate(R.id.action_tracking_to_history)
            }
            tvTrackingAchievementCompleteTest.setOnClickListener {
                onAchievementComplete("test")
            }
        }

        binding.tvTrackingDistanceTest.setOnClickListener {
            trackingViewModel.increaseDistance()
        }

        trackingViewModel.fetchMountainName()
        setObserve()

        with(mapView) {
            onCreate(savedInstanceState)
            mapView.getMapAsync(this@TrackingFragment)
        }
    }

    private fun setObserve() {
        with(trackingViewModel) {
            checkPermission.observe(viewLifecycleOwner, EventObserver {
                if (isPermissionNotGranted()) {
                    requestPermissions()
                } else {
                    trackingViewModel.startService()
                }
            })

            showDialog.observe(viewLifecycleOwner, EventObserver {
                showDialog()
            })

            locationList.observe(viewLifecycleOwner) { locationList ->
                locationCoords = locationList.map { LatLng(it.latitude, it.longitude) }
                updatePath(locationCoords)
            }

            completedAchievementLiveData.observe(viewLifecycleOwner) {
                if (it != null) {
                    onAchievementComplete(it.name)
                }
            }

            statisticsLiveData.observe(viewLifecycleOwner) {
                trackingViewModel.updateAchievement()
            }
        }
    }

    private fun getLastLocation() {
        try {
            fusedLocationClient.lastLocation
                .addOnCompleteListener { task ->
                    if (task.isSuccessful && task.result != null) {
                        val location = LatLng(task.result.latitude, task.result.longitude)
                        Log.d("location", location.toString())
                        this.naverMap?.locationOverlay?.position = LatLng(task.result.latitude, task.result.longitude)
                        this.naverMap?.moveCamera(CameraUpdate.scrollTo(location))
                    } else {
                        Log.e("lastLocation", "Failed to get location.")
                    }
                }
        } catch (unlikely: SecurityException) {
            Log.e("lastLocation", "Lost location permission.$unlikely")
        }
    }

    private fun updatePath(locationCoords: List<LatLng>) {
        if (locationCoords.size >= 2) {
            path.coords = locationCoords
            path.map = naverMap
        }
        if (locationCoords.isNotEmpty()) {
            naverMap?.locationOverlay?.position = locationCoords.last()
            val bounds = LatLngBounds.Builder()
                .include(locationCoords)
                .build()
            moveCamera(bounds)
        }
        if (locationCoords.isEmpty()) {
            path.map = null
        }
    }

    private fun moveCamera(bounds: LatLngBounds) {
        val cameraUpdate = CameraUpdate.fitBounds(bounds, MAP_PADDING)
            .animate(CameraAnimation.Easing)
        naverMap?.moveCamera(cameraUpdate)
    }

    private fun showDialog() {
        val dialog = MountainSelectFragment()
        dialog.show(childFragmentManager, DIALOG)
    }

    private fun isPermissionNotGranted(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) || PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACTIVITY_RECOGNITION
            )
        } else {
            PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        }
    }

    private fun requestPermissions() {
        val shouldProvideRationale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) || ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(),
                Manifest.permission.ACTIVITY_RECOGNITION
            )
        } else {
            ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        }
        if (shouldProvideRationale) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.")
            Snackbar.make(
                binding.root,
                R.string.permission_rationale,
                Snackbar.LENGTH_INDEFINITE
            )
                .setAction(R.string.ok) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        requestPermissions.launch(
                            arrayOf(
                                Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACTIVITY_RECOGNITION
                            )
                        )
                    } else {
                        requestPermissions.launch(
                            arrayOf(
                                Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.ACCESS_FINE_LOCATION
                            )
                        )
                    }
                }
                .show()
        } else {
            Log.i(TAG, "Requesting permission")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                requestPermissions.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACTIVITY_RECOGNITION
                    )
                )
            } else {
                requestPermissions.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    )
                )
            }
        }
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
        trackingViewModel.bindService()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
        trackingViewModel.unbindService()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mapView.onDestroy()
        _binding = null
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onDismiss(dialog: DialogInterface?) {
        trackingViewModel.checkPermission()
    }

    override fun onMapReady(naverMap: NaverMap) {
        this.naverMap = naverMap
        this.naverMap?.locationOverlay?.isVisible = true

        requestPermissions()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        trackingViewModel.locationList.value?.let { locationList ->
            locationCoords = locationList.map { LatLng(it.latitude, it.longitude) }
        }
        updatePath(locationCoords)
        with(path) {
            color = requireContext().getColor(R.color.blue)
            width = MAP_PATH_WIDTH
            outlineWidth = MAP_PATH_OUTLINE_WIDTH
        }
    }

    private fun onAchievementComplete(achievementName: String) {
        AchievementReceiver().notifyAchievementComplete(requireContext(), achievementName)
    }

    companion object {
        private val TAG = TrackingFragment::class.simpleName
        private const val DIALOG = "dialog"
        private const val MAP_PADDING = 50
        private const val MAP_PATH_WIDTH = 30
        private const val MAP_PATH_OUTLINE_WIDTH = 0
    }
}
