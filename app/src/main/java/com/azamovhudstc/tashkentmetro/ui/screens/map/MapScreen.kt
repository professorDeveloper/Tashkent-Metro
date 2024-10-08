package com.azamovhudstc.tashkentmetro.ui.screens.map

import android.animation.Animator
import android.animation.ObjectAnimator
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.azamovhudstc.tashkentmetro.R
import com.azamovhudstc.tashkentmetro.data.local.shp.AppReference
import com.azamovhudstc.tashkentmetro.data.model.station.Line
import com.azamovhudstc.tashkentmetro.data.model.station.Station
import com.azamovhudstc.tashkentmetro.data.model.station.StationLine
import com.azamovhudstc.tashkentmetro.data.model.station.StationState
import com.azamovhudstc.tashkentmetro.databinding.MapScreenBinding
import com.azamovhudstc.tashkentmetro.ui.activity.MainActivity
import com.azamovhudstc.tashkentmetro.ui.screens.map.sheet.StationTimelineBottomSheet
import com.azamovhudstc.tashkentmetro.utils.BaseFragment
import com.azamovhudstc.tashkentmetro.utils.LocalData
import com.azamovhudstc.tashkentmetro.utils.LocalData.metro
import com.azamovhudstc.tashkentmetro.utils.LocalData.popularStations
import com.azamovhudstc.tashkentmetro.utils.custom.StationFilter
import com.azamovhudstc.tashkentmetro.utils.gone
import com.azamovhudstc.tashkentmetro.utils.select
import com.azamovhudstc.tashkentmetro.utils.unSelect
import com.azamovhudstc.tashkentmetro.utils.visible
import com.azamovhudstc.tashkentmetro.viewmodel.search.SearchViewModel
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
import javax.inject.Inject

class MapScreen : BaseFragment<MapScreenBinding>(MapScreenBinding::inflate), OnMapReadyCallback,
    PopularStationAdapter.OnItemClickListener {

    private lateinit var tashkentBounds: LatLngBounds
    private val adapter by lazy { PopularStationAdapter(this) }
    private val viewModel by viewModels<SearchViewModel>()
    private var isFrom = true
    private val polylines = mutableListOf<Polyline>()
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

    override fun onViewCreate() {
        mapView = binding.map
        bottomSheetDialog = BottomSheetDialog(requireContext())
        mapView.onCreate(null)
        mapView.getMapAsync(this)
        binding.bottomSheet.gone()

        binding.mapStyle.setOnClickListener {
            showMapTypeBottomSheet()
        }

        binding.bottomSheet.setOnClickListener {
            if (isSheetVisible) {
                hideBottomSheet()
            } else {
                showBottomSheet()
            }
//            isSheetVisible = !isSheetVisible
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
            }else{

            }
        }

        binding.showDetailRouteButton.setOnClickListener {
            viewModel.bothValues.value?.let { it1 -> drawMapWithDirection(it1) }
        }

        binding.buttonTo.setOnClickListener {
            showInputSearchBottomSheet(viewModel.fromTv.value)
            isFrom = false
        }

        binding.buttonRemoveFrom.setOnClickListener {
            viewModel.clearFromValue()
        }
        binding.buttonRemoveTo.setOnClickListener {
            viewModel.clearToValue()
        }

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
            binding.bottomSheet,
            "translationY",
            binding.bottomSheet.height.toFloat()
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
        val cameraPosition = CameraPosition.Builder()
            .target(stationLatLng)
            .zoom(15f)
            .bearing(90f)
            .tilt(45f)
            .build()

        mMap.animateCamera(
            CameraUpdateFactory.newCameraPosition(cameraPosition),
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

    }

    private fun updateUiToStation(station: Station?) {
        if (station != null) {
            binding.buttonTo.text = station.name
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
            binding.buttonFrom.text = station.name
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

        applyMapStyleBasedOnTheme(requireContext(), mMap)

        setupMetroLines()

        val southWest = LatLng(41.2000, 69.1200)  // Janubi-g'arbiy nuqta
        val northEast = LatLng(41.3800, 69.3700)  // Shimoli-sharqiy nuqta

        tashkentBounds = LatLngBounds(southWest, northEast)


        mMap.setLatLngBoundsForCameraTarget(tashkentBounds)

        mMap.setOnMarkerClickListener { marker ->
            handleMarkerClick(marker)
            true // Return true to indicate that the click was handled
        }

        mMap.setOnCameraMoveListener {
//            val currentPosition = mMap.cameraPosition.target
//            if (!tashkentBounds.contains(currentPosition)) {
//                val moveBack = CameraUpdateFactory.newLatLngBounds(tashkentBounds, 0)
//                mMap.animateCamera(moveBack)
//            }
        }
    }

    fun findAdjacentStations(
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
        return Pair(null, null) // Bekat topilmagan bo'lsa
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
                previousStation?.name ?: "Last station"
            binding.bottomDetailTwoStation.nextStation.text = nextStation?.name ?: "Last station"
            binding.bottomDetailTwoStation.currentStation.text = it.name

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
        val icon =
            station?.state?.let { createStationIcon(it) } // Using OVERGROUND state just as an example

        marker.setIcon(icon)
    }

    private fun moveCameraToMarker(marker: Marker) {
        val cameraPosition = CameraPosition.Builder()
            .target(marker.position)
            .zoom(15f) // Zoom level
            .bearing(90f) // Rotation angle
            .tilt(45f) // Tilt angle
            .build()

        mMap.animateCamera(
            CameraUpdateFactory.newCameraPosition(cameraPosition),
            1500,
            object : GoogleMap.CancelableCallback {
                override fun onFinish() {
                    // You can add custom behavior when the animation finishes
                }

                override fun onCancel() {
                    // Handle if the animation is cancelled
                }
            }
        )
    }

    private fun changeMarkerIconColorToOrange(marker: Marker, station: Station?) {

        val orangeColor = Color.rgb(255, 165, 0) // Orange color
        val icon =
            station?.state?.let { createStationIcon(it, orangeColor) }


        marker.setIcon(icon)

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

    private fun updateOpacity(isReducedOpacity: Boolean) {
        val opacityValue = if (isReducedOpacity) 120 else 255
        val markerAlpha = if (isReducedOpacity) 0.4f else 1.0f

        polylines.forEach { polyline ->
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

    private fun setupMetroLine(line: StationLine, isReducedOpacity: Boolean = false) {
        val polylineOptions = PolylineOptions()

        line.stations.forEach { station ->
            val position = LatLng(station.location.latitude, station.location.longitude)
            polylineOptions.add(position)

            val icon = createStationIcon(station.state)

            val markerOptions = MarkerOptions()
                .position(position)
                .title("${station.name} - ${line.line}")
                .snippet(getTrainStatus(station.name))
                .icon(icon)


            val marker = mMap.addMarker(markerOptions)
            marker?.tag = station

            marker?.let { markers.add(it) }
        }

        val polylineColor = getLineColor(line.line)  // Normal rang


        val polyline = mMap.addPolyline(polylineOptions.color(polylineColor).width(14f))
        line.stations.forEach { station ->
            polyline.tag = station
        }
        polylines.add(polyline)
    }

    private fun createStationIcon(
        status: StationState,
        customTintColor: Int? = null
    ): BitmapDescriptor? {
        val vectorDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.icon_metro)
        vectorDrawable?.setBounds(
            0,
            0,
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight
        )

        // Use the custom tint color if provided, otherwise use status-based color
        val tintColor =
            customTintColor ?: if (status == StationState.UNDERGROUND) Color.RED else Color.BLUE
        vectorDrawable?.setTint(tintColor)

        val bitmap = vectorDrawable?.let {
            Bitmap.createBitmap(
                it.intrinsicWidth,
                it.intrinsicHeight,
                Bitmap.Config.ARGB_8888
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
            Line.YUNUSOBOD -> requireContext().getColor(R.color.map_green)
            Line.INDEPENDENCEDAY -> requireContext().getColor(R.color.map_yellow)
        }
    }

    private fun getTrainStatus(stationName: String): String {
        val status =
            LocalData.trainsStatusList.find { it.from == stationName || it.to == stationName }?.status
        return status ?: "Ma'lumot yo'q"
    }


    private fun showMapStationBottomSheet() {
//        val view = layoutInflater.inflate(R.layout.map_station_bottom_sheet, null)
//        bottomSheetDialog.setContentView(view)
//        bottomSheetDialog.show()
//        bottomSheetDialog.setOnShowListener {
//            val bottomSheet =
//                bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
//            bottomSheet?.let {
//                val behavior = BottomSheetBehavior.from(it)
//                val displayMetrics = resources.displayMetrics
//                val screenHeight = displayMetrics.heightPixels
//                val bottomSheetHeight = (screenHeight * 0.9).toInt()
//                behavior.peekHeight = bottomSheetHeight
//            }
//        }
//        bottomSheetDialog.show()
        binding.bottomDetailTwoStation.mainBottom.visible()

    }

    private fun showInputSearchBottomSheet(value: Station?) {
        val view = layoutInflater.inflate(R.layout.search_bottom_dialog, null)
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


        val popularStationRv = view.findViewById<RecyclerView>(R.id.popular_station_rv)

        val stationLineTv = view.findViewById<TextView>(R.id.station_line)
        val popularTextFrame = view.findViewById<FrameLayout>(R.id.popular_station_frame)
        val popularDivider = view.findViewById<MaterialDivider>(R.id.popular_divider)
        val viewGradient = view.findViewById<View>(R.id.gradient_view)
        val layoutManager = LinearLayoutManager(context)
        viewGradient.background =
            drawGradient(ContextCompat.getColor(requireContext(), R.color.map_red))
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
                        stationLineTv.text = station.line.name
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
                stationLineTv.text = filteredStations[0].line.name
                adapter.submitList(filteredStations, false, value)
            } else {
                isPopular = true
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

    private fun showMapTypeBottomSheet() {
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        val view = layoutInflater.inflate(R.layout.layout_bottom_sheet_map_type, null)


        val normalMapOption = view.findViewById<MaterialCardView>(R.id.normal_map_option)
        val satelliteMapOption = view.findViewById<MaterialCardView>(R.id.satellite_map_option)
        val buttonClose = view.findViewById<FrameLayout>(R.id.button_close)
        if (mMap.mapType != GoogleMap.MAP_TYPE_SATELLITE) normalMapOption.select() else satelliteMapOption.select()

        normalMapOption.setOnClickListener {
            mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
            normalMapOption.select()
            satelliteMapOption.unSelect()
//            bottomSheetDialog.dismiss()
        }
//
        satelliteMapOption.setOnClickListener {
            mMap.mapType = GoogleMap.MAP_TYPE_SATELLITE
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
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }


    private fun applyMapStyleBasedOnTheme(context: Context, googleMap: GoogleMap) {
        try {

            val styleRes =
                when (context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                    Configuration.UI_MODE_NIGHT_YES -> R.raw.map_style_dark
                    Configuration.UI_MODE_NIGHT_NO -> R.raw.map_style_light
                    else -> R.raw.map_style_light
                }

            val success =
                googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(context, styleRes))
            if (!success) {
                Log.e("MapStyle", "Xarita uslubi muvaffaqiyatsiz o'rnatildi.")
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
            GradientDrawable.Orientation.TL_BR,
            intArrayOf(
                Color.parseColor("#E6F1FF"),
                centerColor,
                Color.parseColor("#E6F1FF")
            )
        )
        gradientDrawable.cornerRadius = 5f
        return gradientDrawable

    }

    override fun onItemClick(station: Station) {
        if (isFrom) viewModel.setFromValue(station) else viewModel.setToValue(station)
    }
}