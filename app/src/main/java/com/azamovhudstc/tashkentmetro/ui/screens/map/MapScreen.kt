package com.azamovhudstc.tashkentmetro.ui.screens.map

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.Log
import androidx.core.content.ContextCompat
import com.azamovhudstc.tashkentmetro.R
import com.azamovhudstc.tashkentmetro.data.model.station.Line
import com.azamovhudstc.tashkentmetro.data.model.station.Station
import com.azamovhudstc.tashkentmetro.data.model.station.StationLine
import com.azamovhudstc.tashkentmetro.data.model.station.StationState
import com.azamovhudstc.tashkentmetro.databinding.MapScreenBinding
import com.azamovhudstc.tashkentmetro.utils.BaseFragment
import com.azamovhudstc.tashkentmetro.utils.LocalData
import com.azamovhudstc.tashkentmetro.utils.select
import com.azamovhudstc.tashkentmetro.utils.unSelect
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.card.MaterialCardView

class MapScreen : BaseFragment<MapScreenBinding>(MapScreenBinding::inflate), OnMapReadyCallback {

    private lateinit var tashkentBounds: LatLngBounds

    private lateinit var mMap: GoogleMap
    override fun onViewCreate() {

        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        binding.mapStyle.setOnClickListener {
            showMapTypeBottomSheet()
        }
    }

    override fun onMapReady(p0: GoogleMap) {
        mMap = p0


        setupMetroLines()


        drawRouteFromUserInput("beruniy", "mashinasozlar")


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
            Line.CHILANZAR -> Color.RED
            Line.UZBEKISTAN -> Color.BLUE
            Line.YUNUSOBOD -> Color.GREEN
            Line.INDEPENDENCEDAY -> Color.YELLOW
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

        bottomSheetDialog.setContentView(view)
        bottomSheetDialog.show()
    }

    fun drawRouteFromUserInput(startStationName: String, endStationName: String) {
        // Foydalanuvchi kiritgan bekat nomlariga mos bekatlarni topamiz
        val startStation = findStationByName(startStationName)
        val endStation = findStationByName(endStationName)

        Log.d("tekshirish", "drawRouteFromUserInput: $startStation va $endStation")
        // Agar bekatlar topilmasa, foydalanuvchiga xabar beramiz
        if (startStation == null || endStation == null) {
            Log.e("Metro", "Bekatlar topilmadi: $startStationName yoki $endStationName noto'g'ri")
            return
        }

        // Marshrutni chizish uchun drawRouteAcrossLines funksiyasini chaqiramiz
        drawRouteAcrossLines(startStation, endStation)
    }

    private fun findStationByName(stationName: String): Station? {
        // Foydalanuvchi kiritgan nom asosida barcha liniyalardan bekat qidiriladi
        return LocalData.metro.flatMap { line -> line.stations }.find {
            Log.d("tekshirish", "findStationByName: ${it.name}")
            it.name == stationName.toLowerCase()
        }
    }

    private fun drawRouteAcrossLines(startStation: Station, endStation: Station) {
        val startLine = findLineByStation(startStation)
        val endLine = findLineByStation(endStation)

        if (startLine == null || endLine == null) {
            // Agar stansiyalar qaysi liniyada ekanligini aniqlay olmasak
            Log.e("Metro", "Bekatlar liniyalarini topishning iloji bo'lmadi")
            return
        }

        if (startLine == endLine) {
            // Agar ikkala bekat bir xil liniyada bo'lsa, oddiy marshrut chizamiz
            drawRouteBetweenStations(startLine, startStation, endStation)
        } else {
            // Transfer bekatni topamiz
            val transferStation = findTransferStation(startLine, endLine)

            Log.d("tekshirish", "drawRouteAcrossLines: transfer $transferStation")
            if (transferStation != null) {
                // Birinchi liniyada marshrutni chizamiz
                drawRouteBetweenStations(startLine, startStation, transferStation)

                // Ikkinchi liniyada marshrutni chizamiz
                drawRouteBetweenStations(endLine, transferStation, endStation)
            } else {
                Log.e("tekshiramiz", "O'tish bekati topilmadi, liniyalar o'rtasida o'tish mavjud emas")
            }
        }
    }

    private fun drawRouteBetweenStations(line: StationLine, station1: Station, station2: Station) {
        val polylineOptions = PolylineOptions()

        // Stansiyalarni marshrutga qo'shamiz
        val startIndex = line.stations.indexOf(station1)
        val endIndex = line.stations.indexOf(station2)

        Log.d("chizamiz", "drawRouteBetweenStations: $startIndex")
        Log.d("chizamiz", "drawRouteBetweenStations: $endIndex")


        // Agar startIndex > endIndex bo'lsa, marshrutni teskari yo'nalishda chizamiz
        val range = if (startIndex < endIndex) startIndex..endIndex else startIndex downTo endIndex

        Log.d("chizamiz", "drawRouteBetweenStations: $range")
        range.forEach { index ->
            val station = line.stations[index]
            val position = LatLng(station.location.latitude, station.location.longitude)
            polylineOptions.add(position)

            // Har bir bekatga marker qo'shamiz
            val markerOptions = MarkerOptions()
                .position(position)
                .title(station.name)

            mMap.addMarker(markerOptions)
        }

        // Chiziq rangini va kengligini belgilaymiz
        polylineOptions.color(Color.BLACK).width(20f)

        // Xaritada polylineni chizamiz
        mMap.addPolyline(polylineOptions)

        // Kamerani birinchi bekatga yaqinlashtiramiz
        val startPosition = LatLng(station1.location.latitude, station1.location.longitude)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startPosition, 12f))
    }

    private fun findLineByStation(station: Station): StationLine? {
        // Stansiyaning qaysi liniyada joylashganligini aniqlash
        return LocalData.metro.find { line -> line.stations.contains(station) }
    }

    private fun findTransferStation(line1: StationLine, line2: StationLine): Station? {
        // Liniyalar orasida umumiy bekat (transfer station) topamiz
        return line1.stations.find { station1 ->
            line2.stations.any { station2 ->
                station1.transferable != null && station1.name == station2.name
            }
        }
    }

}