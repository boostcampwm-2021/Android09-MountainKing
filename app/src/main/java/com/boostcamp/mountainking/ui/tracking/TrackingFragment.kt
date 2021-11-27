package com.boostcamp.mountainking.ui.tracking


import android.Manifest
import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.*
import android.widget.RadioButton
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
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
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.naver.maps.geometry.LatLng
import com.naver.maps.geometry.LatLngBounds
import com.naver.maps.map.*
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.overlay.PathOverlay
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TrackingFragment :
    Fragment(), DialogInterface.OnDismissListener, OnMapReadyCallback,
    NavigationView.OnNavigationItemSelectedListener {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var mapView: MapView? = null
    private val trackingViewModel: TrackingViewModel by viewModels()

    private var _binding: FragmentTrackingBinding? = null
    private val binding get() = _binding!!
    private var locationCoords = listOf<LatLng>()
    private var naverMap: NaverMap? = null
    private val path = PathOverlay()
    private var isTracking = false
    private var initLocation: LatLng? = null

    private val requestPermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { isGranted ->
            if (isGranted.values.all { it }) {
                if (!isTracking) {
                    getLastLocation()
                } else {
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
        binding.lifecycleOwner = viewLifecycleOwner
        trackingViewModel.fetchMountainName()

        setListener()
        setObserve()
        setNavigationView()

        mapView?.onCreate(savedInstanceState)
        mapView?.getMapAsync(this@TrackingFragment)

    }

    private fun setListener() {
        binding.btnOpenDrawer.setOnClickListener {
            if (!binding.dlContainer.isDrawerOpen(GravityCompat.END)) {
                binding.dlContainer.openDrawer(GravityCompat.END)
            }
        }
        val menu = binding.nvSideDrawer.menu
        val actionView = menu.findItem(R.id.rg_map_type).actionView
        val typeNormal = actionView.findViewById<RadioButton>(R.id.btn_type_normal)
        val typeSatellite = actionView.findViewById<RadioButton>(R.id.btn_type_satellite)
        val typeHybrid = actionView.findViewById<RadioButton>(R.id.btn_type_hybrid)

        typeNormal.setOnClickListener { onRadioClick(it) }
        typeSatellite.setOnClickListener { onRadioClick(it) }
        typeHybrid.setOnClickListener { onRadioClick(it) }

    }

    private fun setNavigationView() {
        binding.nvSideDrawer.setNavigationItemSelectedListener(this)
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

            completedAchievementLiveData.observe(viewLifecycleOwner, EventObserver {
                onAchievementComplete(it.name)
            })

            statisticsLiveData.observe(viewLifecycleOwner) {
                trackingViewModel.updateAchievement()
            }

            getLastLocationEvent.observe(viewLifecycleOwner, EventObserver {
                this@TrackingFragment.getLastLocation()
            })

            showSaveEvent.observe(viewLifecycleOwner, EventObserver {
                showSaveSnackbar(it)
            })
        }
    }

    private fun getLastLocation() {
        try {
            fusedLocationClient.lastLocation
                .addOnCompleteListener { task ->
                    if (task.isSuccessful && task.result != null) {
                        initLocation = LatLng(task.result.latitude, task.result.longitude)
                        Log.d("location", initLocation.toString())
                        this.naverMap?.locationOverlay?.position =
                            LatLng(task.result.latitude, task.result.longitude)
                        initLocation?.let {
                            this.naverMap?.moveCamera(
                                CameraUpdate.scrollTo(it).animate(CameraAnimation.Easing)
                            )
                        }
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
        val dialog = MountainSelectFragment.newInstance(initLocation)
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
        mapView?.onStart()
        trackingViewModel.bindService()
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
        trackingViewModel.unbindService()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mapView?.onDestroy()
        _binding = null
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView?.onLowMemory()
    }

    override fun onDismiss(dialog: DialogInterface?) {
        trackingViewModel.checkPermission()
    }

    private fun onRadioClick(view: View) {
        if (view is RadioButton) {
            val isChecked = view.isChecked

            when (view.id) {
                R.id.btn_type_normal -> {
                    if (isChecked) {
                        naverMap?.mapType = NaverMap.MapType.Basic
                    }
                }
                R.id.btn_type_satellite -> {
                    if (isChecked) {
                        naverMap?.mapType = NaverMap.MapType.Satellite
                    }
                }
                R.id.btn_type_hybrid -> {
                    if (isChecked) {
                        naverMap?.mapType = NaverMap.MapType.Hybrid
                    }
                }
            }
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.tracking_history -> {
                findNavController().navigate(R.id.action_tracking_to_history)
            }
            R.id.open_source -> {
                startActivity(Intent(requireContext(), OssLicensesMenuActivity::class.java))
            }
        }
        return false
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onMapReady(naverMap: NaverMap) {
        val uiSettings = naverMap.uiSettings
        with(uiSettings) {
            logoGravity = Gravity.START
            isLogoClickEnabled = true
        }

        this.naverMap = naverMap
        this.naverMap?.locationOverlay?.isVisible = true
        this.naverMap?.locationOverlay?.icon = OverlayImage.fromResource(R.drawable.ic_hiking)

        binding.mvNaver.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_MOVE -> {
                    Log.d(
                        "touch",
                        "${this.naverMap?.locationOverlay?.bearing} ${this.naverMap?.cameraPosition?.bearing}"
                    )
                    this.naverMap?.locationOverlay?.bearing =
                        (this.naverMap?.cameraPosition?.bearing ?: 0).toFloat()
                }
            }
            false
        }

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
        initMapState()
    }

    private fun initMapState() {
        val menu = binding.nvSideDrawer.menu
        val actionView = menu.findItem(R.id.rg_map_type).actionView
        onRadioClick(actionView.findViewById(R.id.btn_type_normal))
        onRadioClick(actionView.findViewById(R.id.btn_type_satellite))
        onRadioClick(actionView.findViewById(R.id.btn_type_hybrid))
    }

    private fun onAchievementComplete(achievementName: String) {
        AchievementReceiver().notifyAchievementComplete(requireContext(), achievementName)
    }

    private fun showSaveSnackbar(isSaved: Boolean) {
        val string = if (isSaved) {
            getString(R.string.save_success)
        } else {
            getString(R.string.save_fail)
        }
        Snackbar.make(binding.root, string, Snackbar.LENGTH_SHORT).show()
    }


    companion object {
        private val TAG = TrackingFragment::class.simpleName
        private const val DIALOG = "dialog"
        private const val MAP_PADDING = 50
        private const val MAP_PATH_WIDTH = 30
        private const val MAP_PATH_OUTLINE_WIDTH = 0
    }

}
