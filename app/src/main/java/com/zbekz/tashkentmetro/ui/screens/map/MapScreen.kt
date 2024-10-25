package com.zbekz.tashkentmetro.ui.screens.map

import android.Manifest
import android.animation.Animator
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.GradientDrawable
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zbekz.tashkentmetro.R
import com.zbekz.tashkentmetro.databinding.MapScreenBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.android.material.divider.MaterialDivider
import com.mostafa_anter.marker.CustomMarker
import com.zbekz.tashkentmetro.custom.markerWithText.MarkerInfo
import com.zbekz.tashkentmetro.data.local.shp.AppReference
import com.zbekz.tashkentmetro.data.local.shp.ThemeStyle
import com.zbekz.tashkentmetro.data.model.station.Line
import com.zbekz.tashkentmetro.data.model.station.Station
import com.zbekz.tashkentmetro.data.model.station.StationLine
import com.zbekz.tashkentmetro.data.model.station.StationState
import com.zbekz.tashkentmetro.ui.activity.MainActivity
import com.zbekz.tashkentmetro.ui.screens.map.sheet.StationTimelineBottomSheet
import com.zbekz.tashkentmetro.utils.BaseFragment
import com.zbekz.tashkentmetro.utils.LocalData
import com.zbekz.tashkentmetro.utils.LocalData.metro
import com.zbekz.tashkentmetro.utils.LocalData.popularStations
import com.zbekz.tashkentmetro.utils.custom.StationFilter
import com.zbekz.tashkentmetro.utils.formatString
import com.zbekz.tashkentmetro.utils.gone
import com.zbekz.tashkentmetro.utils.localizeLastStation
import com.zbekz.tashkentmetro.utils.select
import com.zbekz.tashkentmetro.utils.snackString
import com.zbekz.tashkentmetro.utils.unSelect
import com.zbekz.tashkentmetro.utils.visible
import com.zbekz.tashkentmetro.viewmodel.search.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MapScreen : BaseFragment<MapScreenBinding>(MapScreenBinding::inflate), OnMapReadyCallback,
    PopularStationAdapter.OnItemClickListener {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val LOCATION_PERMISSION_REQUEST_CODE = 1001

    private lateinit var tashkentBounds: LatLngBounds
    private val adapter by lazy { PopularStationAdapter(this) }
    private val viewModel by viewModels<SearchViewModel>()
    private var isFrom = true
    private val polyline = mutableListOf<Polyline>()
    private var myLoc =LatLng(0.0,0.0)
    private val markers = mutableListOf<Marker>()
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private val originalPolylineColors = mutableMapOf<Polyline, Int>()
    private var lastSelectedMarker: Marker? = null
    private var isPopular = true
    private var isSheetVisible = false


    @Inject
    lateinit var userPreferenceManager: AppReference
    private lateinit var mapView: MapView
    private lateinit var mMap: GoogleMap

    override fun onViewCreate(savedInstanceState: Bundle?) {
        mapView = binding.map
        bottomSheetDialog = BottomSheetDialog(requireContext())
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        binding.bottomSheet.gone()

        binding.mapStyle.setOnClickListener {
            showMapTypeBottomSheet()
        }

        binding.myLocation.setOnClickListener {
            checkLocationPermissionAndEnableLocation()

        }

        binding.bottomSheet.setOnClickListener {
            if (isSheetVisible) {
                hideBottomSheet()
            } else {
                showBottomSheet()
            }
        }

        binding.searchView.onStationSelected = { station ->
            moveCameraToStation(station)
        }
        binding.buttonCenterCamera.setOnClickListener {
            setupCameraToCenter()
        }

        viewModel.fromTv.observe(viewLifecycleOwner) {
            updateUiFromStation(it)
        }
        viewModel.toTv.observe(viewLifecycleOwner) {
            updateUiToStation(it)
        }

        viewModel.bothValues.observe(viewLifecycleOwner) { pair ->
            if (pair != null) {
                drawMapWithDirection(pair)
            } else {
                clearMap()
            }
        }

        binding.buttonFrom.setOnClickListener {
            if (lastSelectedMarker == null) {
                showInputSearchBottomSheet(viewModel.toTv.value)
                isFrom = true
            } else {
                val station = lastSelectedMarker?.tag as Station
                showCustomMenu(binding.buttonFrom, station, viewModel.fromTv.value, true)

            }
        }

        binding.showDetailRouteButton.setOnClickListener {
            viewModel.bothValues.value?.let { it1 -> drawMapWithDirection(it1) }
        }

        binding.buttonTo.setOnClickListener {
            if (lastSelectedMarker == null) {
                showInputSearchBottomSheet(viewModel.fromTv.value)
                isFrom = false
            } else {
                val station = lastSelectedMarker?.tag as Station
                showCustomMenu(binding.buttonTo, station, viewModel.toTv.value, false)
            }

        }

        binding.buttonRemoveFrom.setOnClickListener {
            viewModel.clearFromValue()
        }
        binding.buttonRemoveTo.setOnClickListener {
            viewModel.clearToValue()
        }

    }

    @SuppressLint("InflateParams")
    private fun showCustomMenu(view: View, station: Station, value: Station?, b: Boolean) {
        val popupView =
            LayoutInflater.from(requireContext()).inflate(R.layout.popup_layout_menu, null)
        val popupWindow = PopupWindow(
            popupView,
            (binding.pp.width / 2),
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )

        val delete = popupView.findViewById<LinearLayout>(R.id.menu_delete)
        val searchStation = popupView.findViewById<LinearLayout>(R.id.menu_search)
        val stationName = popupView.findViewById<LinearLayout>(R.id.menu_station_name)

        if (value == null) {
            delete.gone()
            popupView.findViewById<MaterialDivider>(R.id.menu_delete_divider).gone()
        } else {
            delete.visible()
            popupView.findViewById<MaterialDivider>(R.id.menu_delete_divider).visible()
        }

        searchStation.setOnClickListener {
            showInputSearchBottomSheet(value)
            isFrom = b
            popupWindow.dismiss()
        }

        stationName.setOnClickListener {
            if (b) {
                viewModel.setFromValue(station)
            } else {
                viewModel.setToValue(station)
            }
            popupWindow.dismiss()
        }

        val shouldEnable =
            !(b && viewModel.toTv.value == station) && viewModel.fromTv.value != station

        stationName.isEnabled = shouldEnable
        stationName.alpha = if (shouldEnable) 1f else 0.4f

        popupView.findViewById<TextView>(R.id.menu_station_name_tv).text =
            formatString(station.name, requireContext())

        delete.setOnClickListener {
            if (b) {
                viewModel.clearFromValue()
            } else {
                viewModel.clearToValue()
            }
            popupWindow.dismiss()
        }

        popupWindow.contentView.measure(
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        )


        val location = IntArray(2)

        view.getLocationOnScreen(location)

        val x = location[0]
        val y = (location[1] - (popupWindow.contentView.measuredHeight * 1.02)).toInt()

        popupWindow.showAtLocation(binding.buttonFrom, Gravity.NO_GRAVITY, x, y)
    }

    private fun showBottomSheet() {
        (activity as? MainActivity)?.hideBottomNavigation()
        binding.bottomSheet.visibility = View.VISIBLE
        binding.bottomSheet.translationY = binding.bottomSheet.height.toFloat()
        val animator = ObjectAnimator.ofFloat(binding.bottomSheet, "translationY", 0f)
        animator.duration = 300
        animator.start()
        isSheetVisible = true

    }


    private fun hideBottomSheet() {


        val animator = ObjectAnimator.ofFloat(
            binding.bottomSheet, "translationY", binding.bottomSheet.height.toFloat()
        )
        animator.duration = 300
        animator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {}
            override fun onAnimationEnd(animation: Animator) {
                binding.bottomSheet.visibility = View.GONE
                (activity as? MainActivity)?.showBottomNavigation()
            }

            override fun onAnimationCancel(animation: Animator) {}
            override fun onAnimationRepeat(animation: Animator) {}
        })
        val station = lastSelectedMarker?.tag as? Station
        lastSelectedMarker?.let { resetMarkerColor(it, station) }
        lastSelectedMarker = null
        animator.start()
        isSheetVisible = false
    }


    private fun clearMap() {
        updateOpacity(false)
    }

    private fun moveCameraToStation(station: Station) {
        val stationLatLng = LatLng(station.location.latitude, station.location.longitude)
        val cameraPosition =
            CameraPosition.Builder().target(stationLatLng).zoom(15f).bearing(90f).tilt(45f).build()

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition),
            1500,
            object : GoogleMap.CancelableCallback {
                override fun onFinish() {

                }

                override fun onCancel() {

                }
            })
    }


    private fun showLineBottomSheet(result: MutableList<StationLine>) {
        val bottomSheetFragment = StationTimelineBottomSheet(result)
        bottomSheetFragment.show(parentFragmentManager, bottomSheetFragment.tag)

    }

    private fun drawMapWithDirection(pair: Pair<Station, Station>) {
        val fromStation = pair.first
        val toStation = pair.second
        getDirections(fromStation, toStation)
        hideBottomSheet()

    }

    private fun updateUiToStation(station: Station?) {
        if (station != null) {
            binding.buttonTo.text = formatString(station.name, requireContext())
            binding.buttonRemoveTo.visible()
            bottomSheetDialog.dismiss()
        } else {
            binding.buttonTo.text = ""
            binding.buttonRemoveTo.gone()
            bottomSheetDialog.dismiss()
        }
    }

    private fun updateUiFromStation(station: Station?) {
        if (station != null) {
            binding.buttonFrom.text = formatString(station.name, requireContext())
            binding.buttonRemoveFrom.visible()
            bottomSheetDialog.dismiss()
        } else {
            binding.buttonFrom.text = ""
            binding.buttonRemoveFrom.gone()
            bottomSheetDialog.dismiss()
        }

    }


    override fun onMapReady(p0: GoogleMap) {
        mMap = p0

        binding.mapFloatingMarkersOverlay.setSource(mMap)
        applyMapStyleBasedOnTheme(requireContext(), mMap)

//        setupMetroLines()

        val southWest = LatLng(41.2000, 69.1200)  // Janubi-g'arbiy nuqta
        val northEast = LatLng(41.3800, 69.3700)  // Shimoli-sharqiy nuqta

        tashkentBounds = LatLngBounds(southWest, northEast)


        mMap.setLatLngBoundsForCameraTarget(tashkentBounds)

        mMap.setOnMarkerClickListener { marker ->
            if (!(myLoc.latitude == marker.position.latitude && myLoc.longitude == marker.position.longitude)) {
                handleMarkerClick(marker)
            }
            true // Return true to indicate that the click was handled
        }

    }

    private fun findAdjacentStations(
        station: Station
    ): Pair<Station?, Station?> {
        metro.forEach { stationLine ->
            val stations = stationLine.stations
            val index = stations.indexOfFirst { it.id == station.id }
            if (index != -1) {
                val previousStation = if (index > 0) stations[index - 1] else null
                val nextStation = if (index < stations.size - 1) stations[index + 1] else null
                return Pair(previousStation, nextStation)
            }
        }
        return Pair(null, null)
    }


    private fun handleMarkerClick(marker: Marker) {

        val station = marker.tag as? Station

        if (!isSheetVisible) {
            showBottomSheet()
        }
        station?.let {
            val result = findAdjacentStations(it)
            val previousStation = result.first
            val nextStation = result.second
            binding.bottomDetailTwoStation.previousStation.text =
                formatString(previousStation?.name ?: "", requireContext()) ?: localizeLastStation()
            binding.bottomDetailTwoStation.nextStation.text =
                formatString(nextStation?.name ?: "", requireContext()) ?: localizeLastStation()
            binding.bottomDetailTwoStation.currentStation.text =
                formatString(it.name, requireContext())

            binding.bottomDetailTwoStation.previousStation.setOnClickListener {
                previousStation?.let { prev ->
                    val previousMarker = findMarkerByStation(prev)
                    previousMarker?.let { prev1 -> handleMarkerClick(prev1) }
                }
            }

            binding.bottomDetailTwoStation.nextStation.setOnClickListener {
                nextStation?.let { next ->
                    val nextMarker = findMarkerByStation(next)
                    nextMarker?.let { next1 -> handleMarkerClick(next1) }
                }
            }
        }

        lastSelectedMarker?.let { resetMarkerColor(it, station) }

        changeMarkerIconColorToOrange(marker, station)


        moveCameraToMarker(marker)


        lastSelectedMarker = marker
    }

    private fun findMarkerByStation(station: Station): Marker? {
        return markers.find { marker ->
            val markerStation = marker.tag as? Station
            markerStation == station
        }
    }

    private fun resetMarkerColor(marker: Marker, station: Station?) {
        val defaultColor = station?.state?.let {
            if (it == StationState.UNDERGROUND) Color.RED else Color.BLUE
        }
        val icon = station?.state?.let { createStationIcon(it, defaultColor) }
        marker.setIcon(icon)
    }

    private fun moveCameraToMarker(marker: Marker) {
        val cameraPosition =
            CameraPosition.Builder().target(marker.position).zoom(15f) // Zoom level
                .bearing(90f) // Rotation angle
                .tilt(45f) // Tilt angle
                .build()

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition),
            1500,
            object : GoogleMap.CancelableCallback {
                override fun onFinish() {
                }

                override fun onCancel() {
                }
            })
    }

    private fun changeMarkerIconColorToOrange(marker: Marker, station: Station?) {

        val orangeColor = Color.rgb(255, 165, 0) // Orange color
        val icon = station?.state?.let { createStationIcon(it, orangeColor) }

        if (icon != null) {
            marker.alpha = 1f
            marker.setIcon(icon)
            Log.d("marker", "changeMarkerIconColorToOrange: ")
        } else {
            Log.e("Marker", "Icon is null, check createStationIcon method")
        }
//        marker.setIcon(icon)

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                // Permission granted, now enable location
                checkLocationPermissionAndEnableLocation()
            } else {
                snackString("Location permission denied")
            }
        }
    }

    private fun checkLocationPermissionAndEnableLocation() {
        if (ContextCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        } else {
            if (isLocationEnabled()) {
                enableUserLocation()
            } else {
                // Prompt the user to enable GPS
                Toast.makeText(
                    requireActivity(),
                    "Please enable location services",
                    Toast.LENGTH_SHORT
                ).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        }
    }

    private fun enableUserLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                val currentLatLng = LatLng(location.latitude, location.longitude)
                addCustomMarker(currentLatLng, "you")
                myLoc =currentLatLng
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f))
            } else {
                snackString("Unable to get current location")
            }
        }
    }

    private fun addCustomMarker(location: LatLng, timeText: String) {

        CustomMarker.Builder()
            .context(requireActivity())
            .avatar("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQNL_ZnOTpXSvhf1UaK7beHey2BX42U6solRA&s") //your avatar url
            .googleMap(mMap)

            .lat(location.latitude) // your latitude
            .long(location.longitude)
            .googleMap(mMap)// your longitude
            .build()
    }



    private fun updateOpacity(isReducedOpacity: Boolean) {
        val opacityValue = if (isReducedOpacity) 120 else 255
        val markerAlpha = if (isReducedOpacity) 0.4f else 1.0f

        polyline.forEach { polyline ->
            if (!originalPolylineColors.containsKey(polyline)) {
                originalPolylineColors[polyline] = polyline.color
            }

            val originalColor = originalPolylineColors[polyline] ?: polyline.color
            val updatedColor = if (isReducedOpacity) {
                getLineColorWithOpacity(originalColor, opacityValue)
            } else {
                originalColor
            }
            polyline.color = updatedColor
        }

        markers.forEach { marker ->
            marker.alpha = markerAlpha
        }


    }
    private fun setupMetroLines() {
        metro.forEach { line ->
            setupMetroLine(line)
        }
        setupCameraToCenter()
    }


    private fun setupCameraToCenter() {
        val centralStation = LatLng(41.29374972350616, 69.2807024717331)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(centralStation, 11.5f))
    }

    private fun setupMetroLine(line: StationLine, isReducedOpacity: Boolean = false) {
        val polylineOptions = PolylineOptions()

        line.stations.forEach { station ->
            val position = LatLng(station.location.latitude, station.location.longitude)
            polylineOptions.add(position)

            val icon = createStationIcon(station.state)

            val id = station.id.toLong()
            val latLang = LatLng(station.location.latitude, station.location.longitude)
            val title = formatString(station.name, requireContext())
            val color = if (userPreferenceManager.theme == ThemeStyle.DARK) ContextCompat.getColor(
                requireContext(),
                R.color.white
            )
            else ContextCompat.getColor(requireContext(), R.color.black)
            val markerInfo = MarkerInfo(latLang, title, color)


            val markerOptions =
                MarkerOptions()
                    .position(markerInfo.coordinates)
                    .title("${formatString(station.name, requireContext())} - ${line.line}")
                    .snippet(getTrainStatus(formatString(station.name, requireContext())))
                    .icon(icon)


            val marker = mMap.addMarker(markerOptions)
            binding.mapFloatingMarkersOverlay.addMarker(id, markerInfo)
            marker?.tag = station

            marker?.let { markers.add(it) }
        }

        val polylineColor = getLineColor(line.line)  // Normal rang


        val polyline = mMap.addPolyline(polylineOptions.color(polylineColor).width(14f))
        line.stations.forEach { station ->
            polyline.tag = station
        }
        this.polyline.add(polyline)
    }


    private fun createStationIcon(
        status: StationState,
        customTintColor: Int? = null
    ): BitmapDescriptor? {
        val vectorDrawable = ContextCompat.getDrawable(
            requireContext(), R.drawable.icon_metro
        )

        vectorDrawable?.setBounds(
            0, 0, vectorDrawable.intrinsicWidth, vectorDrawable.intrinsicHeight
        )

        val tintColor = customTintColor ?: if (status == StationState.UNDERGROUND) Color.RED else Color.BLUE
        vectorDrawable?.setTint(tintColor)


        val bitmap = vectorDrawable?.let {
            Bitmap.createBitmap(
                it.intrinsicWidth, it.intrinsicHeight, Bitmap.Config.ARGB_8888
            )
        }
        val canvas = bitmap?.let { Canvas(it) }

        val paint = Paint().apply {
            color = Color.WHITE
            style = Paint.Style.FILL
        }

        val radius = vectorDrawable?.intrinsicWidth?.div(2)?.toFloat() ?: 0f
        val centerX = vectorDrawable?.intrinsicWidth?.div(2)?.toFloat() ?: 0f
        val centerY = vectorDrawable?.intrinsicHeight?.div(2)?.toFloat() ?: 0f

        canvas?.drawCircle(centerX, centerY, radius, paint)

        canvas?.let { vectorDrawable.draw(it) }
        Log.d("Bitmap", "Width: ${bitmap?.width}, Height: ${bitmap?.height}")
        return bitmap?.let { BitmapDescriptorFactory.fromBitmap(it) }
    }

    private fun getLineColorWithOpacity(color: Int, opacity: Int): Int {
        val alpha = (opacity * 255) / 255
        return Color.argb(alpha, Color.red(color), Color.green(color), Color.blue(color))
    }

    private fun getLineColor(line: Line): Int {
        return when (line) {
            Line.CHILANZAR -> requireContext().getColor(R.color.map_red)
            Line.UZBEKISTAN -> requireContext().getColor(R.color.map_blue)
            Line.YUNUSABAD -> requireContext().getColor(R.color.map_green)
            Line.INDEPENDENCEDAY -> requireContext().getColor(R.color.map_yellow)
        }
    }

    private fun getTrainStatus(stationName: String): String {
        val status =
            LocalData.trainsStatusList.find { it.from == stationName || it.to == stationName }?.status
        return status ?: "Ma'lumot yo'q"
    }

    @SuppressLint("MissingInflatedId")
    private fun showInputSearchBottomSheet(value: Station?) {
        val view = layoutInflater.inflate(
            R.layout.search_bottom_dialog, null
        )
        bottomSheetDialog.setContentView(view)

        bottomSheetDialog.setOnShowListener {
            val bottomSheet =
                bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.let {
                val behavior = BottomSheetBehavior.from(it)
                val displayMetrics = resources.displayMetrics
                val screenHeight = displayMetrics.heightPixels
                val bottomSheetHeight = (screenHeight * 0.9).toInt()
                behavior.peekHeight = bottomSheetHeight
            }
        }

        val popularStationRv =
            view.findViewById<RecyclerView>(R.id.popular_station_rv)
        val stationLineTv =
            view.findViewById<TextView>(R.id.station_line)
        val popularTextFrame =
            view.findViewById<FrameLayout>(R.id.popular_station_frame)
        val popularDivider =
            view.findViewById<MaterialDivider>(R.id.popular_divider)
        val viewGradient =
            view.findViewById<View>(R.id.gradient_view)
        val layoutManager = LinearLayoutManager(requireContext())
        viewGradient.background = drawGradient(
            ContextCompat.getColor(
                requireContext(), R.color.map_red
            )
        )
        viewGradient.gone()
        popularStationRv.layoutManager = layoutManager
        adapter.submitList(popularStations, true, value)
        popularStationRv.adapter = adapter


        bottomSheetDialog.setOnDismissListener {
            adapter.submitList(popularStations, true, null)
        }

        popularStationRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (newState == RecyclerView.SCROLL_STATE_IDLE && !isPopular) {
                    val lm = recyclerView.layoutManager as LinearLayoutManager
                    val firstVisiblePosition = lm.findFirstVisibleItemPosition()
                    if (firstVisiblePosition != RecyclerView.NO_POSITION) {
                        val (station, gradient) = adapter.getStationAt(firstVisiblePosition)
                        stationLineTv.text = formatString(station.line.name, requireContext())
                        viewGradient.visible()
                        viewGradient.background = gradient
                    }
                } else {
                    viewGradient.gone()
                }
            }
        })


        val searchEt = view.findViewById<EditText>(R.id.search_et)
        searchEt.addTextChangedListener {
            if (!it.isNullOrEmpty()) {
                isPopular = false
                viewGradient.visible()
                val filteredStations = StationFilter.filterStations(it.toString(), metro)
                if (filteredStations.isNullOrEmpty()) {
                    stationLineTv.gone()
                    popularStationRv.gone()
                    popularDivider.gone()
                    popularTextFrame.gone()
                } else {
                    popularTextFrame.visible()
                    stationLineTv.visible()
                    popularStationRv.visible()
                    stationLineTv.text = filteredStations[0].line.name
                    adapter.submitList(filteredStations, false, value)
                }


            } else {
                isPopular = true
                stationLineTv.visible()
                popularStationRv.visible()
                popularTextFrame.visible()
                popularDivider.visible()
                viewGradient.gone()
                adapter.submitList(popularStations, true, null)

            }
        }
        view.findViewById<MaterialButton>(R.id.button_cancel)
            .setOnClickListener { bottomSheetDialog.dismiss() }
        bottomSheetDialog.show()
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager =
            requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    private fun showMapTypeBottomSheet() {
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        val view = layoutInflater.inflate(
            R.layout.layout_bottom_sheet_map_type, null
        )
        val type = AppReference(requireContext()).mapStyle

        val normalMapOption =
            view.findViewById<MaterialCardView>(R.id.normal_map_option)
        val satelliteMapOption =
            view.findViewById<MaterialCardView>(R.id.satellite_map_option)
        val buttonClose =
            view.findViewById<FrameLayout>(R.id.button_close)
        if (mMap.mapType == GoogleMap.MAP_TYPE_NORMAL) normalMapOption.select() else satelliteMapOption.select()




        normalMapOption.setOnClickListener {
            mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
            AppReference(requireContext()).mapStyle = "normal"

            normalMapOption.select()
            satelliteMapOption.unSelect()
//            bottomSheetDialog.dismiss()
        }
//
        satelliteMapOption.setOnClickListener {
            mMap.setMapStyle(null)
            mMap.mapType = GoogleMap.MAP_TYPE_HYBRID
            AppReference(requireContext()).mapStyle = "satellite"

            normalMapOption.unSelect()
            satelliteMapOption.select()
//            bottomSheetDialog.dismiss()
        }

        buttonClose.setOnClickListener {
            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.setContentView(view)
        bottomSheetDialog.show()
    }


    override fun onResume() {
        super.onResume()
        if (this::mapView.isInitialized ) {
            mapView.onResume()
        }
    }

    override fun onPause() {
        super.onPause()
        if (this::mapView.isInitialized) {
            mapView.onPause()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (this::mapView.isInitialized) {
            mapView.onDestroy()
        }
    }

    override fun onLowMemory() {
        super.onLowMemory()
        if (this::mapView.isInitialized) {
            mapView.onLowMemory()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (this::mapView.isInitialized) {

            mapView.onSaveInstanceState(outState)
        }
    }

    private fun applyMapStyleBasedOnTheme(context: Context, googleMap: GoogleMap) {
        try {
            val reference =AppReference(requireContext())

            val styleRes =
                when (context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                    Configuration.UI_MODE_NIGHT_YES -> R.raw.map_style_dark
                    Configuration.UI_MODE_NIGHT_NO -> R.raw.map_style_light
                    else -> R.raw.map_style_light
                }


            if (reference.mapStyle =="normal") {
                mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
            } else {
                mMap.mapType = GoogleMap.MAP_TYPE_HYBRID
            }

            setupMetroLines()

            val success =
                googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(context, styleRes))
            if (!success) {
                Log.e("MapStyle", "Xarita uslubi muvaffaqiyatsiz o'rnatildi.")
            }else{
                Log.d("mapstyle", "applyMapStyleBasedOnTheme: ")
            }
        } catch (e: Resources.NotFoundException) {
            Log.e("MapStyle", "Xarita uslubini yuklashda xatolik yuz berdi: ", e)
        } catch (e: Exception) {
            Log.e("MapStyle", "Xarita uslubini o'rnatishda umumiy xatolik: ", e)
        }
    }


    private fun getStationById(id: Int): Station? {
        for (line in metro) {
            val station = line.stations.firstOrNull { it.id == id }
            if (station != null) {
                return station
            }
        }
        return null
    }

    private fun getNeighbors(station: Station): List<Station> {
        val neighbors = mutableListOf<Station>()

        val line = metro.firstOrNull { it.line == station.line }
        line?.let {
            val index = it.stations.indexOfFirst { s -> s.id == station.id }
            if (index != -1) {
                if (index > 0) {
                    neighbors.add(it.stations[index - 1])
                }
                if (index < it.stations.size - 1) {
                    neighbors.add(it.stations[index + 1])
                }
            }
        }

        station.transferable?.let { transferableId ->
            getStationById(transferableId)?.let { transferableStation ->
                neighbors.add(transferableStation)
            }
        }

        return neighbors
    }

    private fun getDirections(start: Station, end: Station) {
        val queue = mutableListOf<Pair<Station, List<Station>>>()
        val visited = mutableSetOf<Int>()

        queue.add(Pair(start, listOf(start)))
        visited.add(start.id)

        while (queue.isNotEmpty()) {
            val (current, path) = queue.removeAt(0)

            if (current.id == end.id) {
                val result = mutableListOf<StationLine>()
                var currentLine: StationLine? = null

                for (station in path) {
                    val line = metro.firstOrNull { it.line == station.line }
                    line?.let {
                        if (currentLine == null || currentLine!!.line != it.line) {
                            currentLine?.let { cl ->
                                result.add(cl)
                            }
                            currentLine = StationLine(it.id, it.line, mutableListOf())
                        }
                        currentLine?.stations?.add(station)
                    }
                }

                currentLine?.let { cl ->
                    result.add(cl)
                }

                updateOpacity(true)
                result.forEach { line ->
                    setupMetroLine(line)
                }
                showLineBottomSheet(result)

                return
            }

            for (neighbor in getNeighbors(current)) {
                if (visited.add(neighbor.id)) {
                    queue.add(Pair(neighbor, path + neighbor))
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.clearAllValue()
        (activity as? MainActivity)?.showBottomNavigation()
        binding.searchView.destroyView()
        lastSelectedMarker = null

    }

    private fun drawGradient(centerColor: Int): GradientDrawable {
        val gradientDrawable = GradientDrawable(
            GradientDrawable.Orientation.TL_BR, intArrayOf(
                Color.TRANSPARENT, centerColor, Color.TRANSPARENT
            )
        )
        gradientDrawable.cornerRadius = 5f
        return gradientDrawable

    }

    override fun onItemClick(station: Station) {
        if (isFrom) viewModel.setFromValue(station) else viewModel.setToValue(station)
    }
}