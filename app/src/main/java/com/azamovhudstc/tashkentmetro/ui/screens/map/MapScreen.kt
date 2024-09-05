package com.azamovhudstc.tashkentmetro.ui.screens.map

import android.widget.TextView
import com.azamovhudstc.tashkentmetro.R
import com.azamovhudstc.tashkentmetro.databinding.MapScreenBinding
import com.azamovhudstc.tashkentmetro.utils.BaseFragment
import com.azamovhudstc.tashkentmetro.utils.select
import com.azamovhudstc.tashkentmetro.utils.unSelect
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
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

        val southWest = LatLng(41.2000, 69.1200)  // Janubi-g'arbiy nuqta
        val northEast = LatLng(41.3800, 69.3700)  // Shimoli-sharqiy nuqta

        tashkentBounds = LatLngBounds(southWest, northEast)


        val tashkent = LatLng(41.2995, 69.2401)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(tashkent, 13.0f))

        mMap.setLatLngBoundsForCameraTarget(tashkentBounds)


        mMap.setOnCameraMoveListener {
            val currentPosition = mMap.cameraPosition.target
            if (!tashkentBounds.contains(currentPosition)) {
                val moveBack = CameraUpdateFactory.newLatLngBounds(tashkentBounds, 0)
                mMap.animateCamera(moveBack)
            }
        }
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
}