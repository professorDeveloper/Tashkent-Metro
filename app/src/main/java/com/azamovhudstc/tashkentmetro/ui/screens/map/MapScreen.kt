package com.azamovhudstc.tashkentmetro.ui.screens.map

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.azamovhudstc.tashkentmetro.R
import com.azamovhudstc.tashkentmetro.data.local.shp.AppReference
import com.azamovhudstc.tashkentmetro.data.model.station.Line
import com.azamovhudstc.tashkentmetro.data.model.station.Station
import com.azamovhudstc.tashkentmetro.data.model.station.StationLine
import com.azamovhudstc.tashkentmetro.data.model.station.StationState
import com.azamovhudstc.tashkentmetro.databinding.MapScreenBinding
import com.azamovhudstc.tashkentmetro.utils.BaseFragment
import com.azamovhudstc.tashkentmetro.utils.LocalData
import com.azamovhudstc.tashkentmetro.utils.ViewUtils
import com.azamovhudstc.tashkentmetro.utils.select
import com.azamovhudstc.tashkentmetro.utils.unSelect
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.card.MaterialCardView
import javax.inject.Inject

class MapScreen : BaseFragment<MapScreenBinding>(MapScreenBinding::inflate), OnMapReadyCallback {

    private lateinit var tashkentBounds: LatLngBounds
    @Inject
    lateinit var userPreferenceManager: AppReference
    private lateinit var mapView: MapView
    private lateinit var mMap: GoogleMap
    override fun onViewCreate() {
        mapView = binding.map
        mapView.onCreate(null)
        mapView.getMapAsync(this)
        binding.mapStyle.setOnClickListener {
            showMapTypeBottomSheet()
        }
        binding.buttonCenterCamera.setOnClickListener{
            setupCameraToCenter()
        }
    }



    override fun onMapReady(p0: GoogleMap) {
        mMap = p0
        applyMapStyleBasedOnTheme(requireContext(),mMap)

        setupMetroLines()


//        drawRouteFromUserInput("beruniy", "mashinasozlar")


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
        LocalData.metro.forEach { line ->
            setupMetroLine(line)
        }

        setupCameraToCenter()
    }


    private fun setupCameraToCenter(){
        val centralStation = LatLng(41.29374972350616, 69.2807024717331)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(centralStation, 11.5f))
    }

    private fun setupMetroLine(line: StationLine) {
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

            mMap.addMarker(markerOptions)
        }

        polylineOptions.color(getLineColor(line.line)).width(14f)
        mMap.addPolyline(polylineOptions)
    }

    private fun createStationIcon(status: StationState): BitmapDescriptor? {
        val vectorDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.icon_metro)
        vectorDrawable?.setBounds(
            0,
            0,
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight
        )

        if (status == StationState.UNDERGROUND) vectorDrawable?.setTint(Color.RED) else vectorDrawable?.setTint(Color.BLUE)


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
        return LocalData.metro.flatMap { line -> line.stations }.find {
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
        return LocalData.metro.find { line -> line.stations.contains(station) }
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
            // Tizim mavzusini aniqlash (kunduzgi yoki tungi rejim)
            val nightModeFlags = context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK

            // Tizimning mavzusiga qarab tegishli xarita uslubini o'rnatish
            val styleRes = when (nightModeFlags) {
                Configuration.UI_MODE_NIGHT_YES -> R.raw.map_style_dark  // Tungi mavzu uchun uslub
                Configuration.UI_MODE_NIGHT_NO -> R.raw.map_style_light      // Kunduzgi mavzu uchun uslub
                else -> R.raw.map_style_light                               // Belgilanmagan bo'lsa, kunduzgi uslub
            }

            // Xarita uslubini qo'llash
            val success = googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(context, styleRes))
            if (!success) {
                Log.e("MapStyle", "Xarita uslubi muvaffaqiyatsiz o'rnatildi.")
            }
        } catch (e: Resources.NotFoundException) {
            Log.e("MapStyle", "Xarita uslubini yuklashda xatolik yuz berdi: ", e)
        } catch (e: Exception) {
            Log.e("MapStyle", "Xarita uslubini o'rnatishda umumiy xatolik: ", e)
        }
    }


}