package com.azamovhudstc.tashkentmetro.ui.screens.map

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
import com.azamovhudstc.tashkentmetro.data.model.station.StationLocation
import com.azamovhudstc.tashkentmetro.data.model.station.StationState
import com.azamovhudstc.tashkentmetro.databinding.MapScreenBinding
import com.azamovhudstc.tashkentmetro.utils.BaseFragment
import com.azamovhudstc.tashkentmetro.utils.LocalData
import com.azamovhudstc.tashkentmetro.utils.LocalData.metro
import com.azamovhudstc.tashkentmetro.utils.LocalData.popularStations
import com.azamovhudstc.tashkentmetro.utils.custom.StationFilter
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
    lateinit var bottomSheetDialog: BottomSheetDialog

    @Inject
    lateinit var userPreferenceManager: AppReference
    private lateinit var mapView: MapView
    private lateinit var mMap: GoogleMap

    override fun onViewCreate() {
        mapView = binding.map
        bottomSheetDialog = BottomSheetDialog(requireContext())
        mapView.onCreate(null)
        mapView.getMapAsync(this)
        binding.mapStyle.setOnClickListener {
            showMapTypeBottomSheet()
        }
        binding.buttonCenterCamera.setOnClickListener {
            setupCameraToCenter()
        }

        viewModel.fromTv.observe(viewLifecycleOwner){
            updateUiFromStation(it)
        }
        viewModel.toTv.observe(viewLifecycleOwner){
            updateUiToStation(it)
        }
        viewModel.bothValues.observe(viewLifecycleOwner) { pair ->
            drawMapWithDirection(pair)
        }

        binding.buttonFrom.setOnClickListener {
            showInputSearchBottomSheet()
            isFrom = true
        }

        binding.buttonTo.setOnClickListener {
            showInputSearchBottomSheet()
            isFrom = false
        }

    }

    private fun drawMapWithDirection(pair: Pair<Station, Station>) {
        val fromStation = pair.first
        val toStation = pair.second
        getDirections(fromStation, toStation)
    }

    private fun updateUiToStation(station: Station?) {
        binding.buttonTo.text = station?.name
        binding.buttonRemoveTo.visible()
        bottomSheetDialog.dismiss()
    }

    private fun updateUiFromStation(station: Station?) {
        binding.buttonFrom.text = station?.name
        binding.buttonRemoveFrom.visible()
        bottomSheetDialog.dismiss()
    }


    override fun onMapReady(p0: GoogleMap) {
        mMap = p0
        applyMapStyleBasedOnTheme(requireContext(), mMap)

        setupMetroLines()

        val southWest = LatLng(41.2000, 69.1200)  // Janubi-g'arbiy nuqta
        val northEast = LatLng(41.3800, 69.3700)  // Shimoli-sharqiy nuqta

        tashkentBounds = LatLngBounds(southWest, northEast)


        mMap.setLatLngBoundsForCameraTarget(tashkentBounds)


        mMap.setOnCameraMoveListener {
            val currentPosition = mMap.cameraPosition.target
            if (!tashkentBounds.contains(currentPosition)) {
                val moveBack = CameraUpdateFactory.newLatLngBounds(tashkentBounds, 0)
                mMap.animateCamera(moveBack)
            }
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

    private fun updateOpacity(isReducedOpacity: Boolean, result: List<StationLine>) {
        polylines.forEach { polyline ->
            val opacityValue = 120
            val updatedColor = getLineColorWithOpacity(polyline.color, opacityValue)
            polyline.color = updatedColor
        }

        markers.forEach { marker ->
            marker.alpha = 0.4f
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
            marker?.tag = line
            marker?.let { markers.add(it) }
        }

        val polylineColor = getLineColor(line.line)  // Normal rang


        val polyline = mMap.addPolyline(polylineOptions.color(polylineColor).width(14f))
        line.stations.forEach { station ->
            polyline.tag = station
        }
        polylines.add(polyline)
    }

    private fun createStationIcon(status: StationState): BitmapDescriptor? {
        val vectorDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.icon_metro)
        vectorDrawable?.setBounds(
            0,
            0,
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight
        )

        if (status == StationState.UNDERGROUND) vectorDrawable?.setTint(Color.RED) else vectorDrawable?.setTint(
            Color.BLUE
        )


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

    private fun getLineColorWithOpacity(color: Int, opacityPercent: Int): Int {
        val alpha = (opacityPercent * 255) / 100
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

    private fun showInputSearchBottomSheet() {
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
        popularStationRv.layoutManager = layoutManager
        popularStationRv.adapter = adapter

        bottomSheetDialog.setOnDismissListener {
            adapter.submitList(popularStations, true)
        }

        popularStationRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {

                    val lm = recyclerView.layoutManager as LinearLayoutManager
                    val firstVisiblePosition = lm.findFirstVisibleItemPosition()
                    if (firstVisiblePosition != RecyclerView.NO_POSITION) {
                        val (station, gradient) = adapter.getStationAt(firstVisiblePosition)
                        stationLineTv.text = station.line.name
                        viewGradient.background = gradient
                    }
                }
            }
        })


        val searchEt = view.findViewById<EditText>(R.id.search_et)
        searchEt.addTextChangedListener {
            if (!it.isNullOrEmpty()) {
//                popularTextFrame.gone()
//                popularDivider.gone()
                val filteredStations = StationFilter.filterStations(it.toString(), metro)
                stationLineTv.text = filteredStations[0].line.name
                adapter.submitList(filteredStations, false)
            } else {
                popularTextFrame.visible()
                popularDivider.visible()
                adapter.submitList(popularStations, true)

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

    fun drawRouteFromUserInput(startStationName: String, endStationName: String) {
        val startStation = findStationByName(startStationName)
        val endStation = findStationByName(endStationName)

        if (startStation == null || endStation == null) {
            Log.e("Metro", "Bekatlar topilmadi")
            return
        }

        drawRouteAcrossLines(startStation, endStation)
    }

    private fun findStationByName(stationName: String): Station? {
        return metro.flatMap { line -> line.stations }.find {
            it.name == stationName.toLowerCase()
        }
    }

    private fun drawRouteAcrossLines(startStation: Station, endStation: Station) {
        val startLine = findLineByStation(startStation)
        val endLine = findLineByStation(endStation)

        if (startLine == null || endLine == null) {
            Log.e("Metro", "Bekatlar liniyalarini topishning iloji bo'lmadi")
            return
        }

        if (startLine == endLine) {
            // Agar ikkala bekat bir xil liniyada bo'lsa, oddiy marshrut chizamiz
            drawRouteBetweenStations(startLine, startStation, endStation)
        } else {
            // Transfer bekatni topamiz
            val transferStation = findTransferStation(startLine, endLine)

            if (transferStation != null) {
                drawRouteBetweenStations(startLine, startStation, transferStation)

                drawRouteBetweenStations(endLine, transferStation, endStation)
            } else {
                Log.e("Metro", "O'tish bekati topilmadi, liniyalar o'rtasida o'tish mavjud emas")
            }
        }
    }

    private fun drawRouteBetweenStations(line: StationLine, station1: Station, station2: Station) {
        val polylineOptions = PolylineOptions()

        val startIndex = line.stations.indexOf(station1)
        val endIndex = line.stations.indexOf(station2)

        val range = if (startIndex < endIndex) startIndex..endIndex else startIndex downTo endIndex

        range.forEach { index ->
            val station = line.stations[index]
            val position = LatLng(station.location.latitude, station.location.longitude)
            polylineOptions.add(position)

            val markerOptions = MarkerOptions()
                .position(position)
                .title(station.name)

            mMap.addMarker(markerOptions)
        }


        polylineOptions.color(Color.BLACK).width(20f)


        mMap.addPolyline(polylineOptions)

        // Kamerani birinchi bekatga yaqinlashtiramiz
        val startPosition = LatLng(station1.location.latitude, station1.location.longitude)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startPosition, 12f))
    }

    private fun findLineByStation(station: Station): StationLine? {
        return metro.find { line -> line.stations.contains(station) }
    }

    private fun findTransferStation(line1: StationLine, line2: StationLine): Station? {
        return line1.stations.find { station1 ->
            line2.stations.any { station2 ->
                station1.transferable != null && station1.name == station2.name
            }
        }
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


    fun applyMapStyleBasedOnTheme(context: Context, googleMap: GoogleMap) {
        try {
            val nightModeFlags =
                context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK

            val styleRes = when (nightModeFlags) {
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

                updateOpacity(true,result)
                result.forEach { line ->
                    setupMetroLine(line)
                }
//                route = result
//                showRouteDetailsView()
                return
            }

            for (neighbor in getNeighbors(current)) {
                if (visited.add(neighbor.id)) {
                    queue.add(Pair(neighbor, path + neighbor))
                }
            }
        }
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