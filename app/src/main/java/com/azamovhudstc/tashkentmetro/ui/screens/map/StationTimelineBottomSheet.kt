package com.azamovhudstc.tashkentmetro.ui.screens.map

import TimelineAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.azamovhudstc.tashkentmetro.data.model.station.EndStation
import com.azamovhudstc.tashkentmetro.data.model.station.MiddleStation
import com.azamovhudstc.tashkentmetro.data.model.station.StartStation
import com.azamovhudstc.tashkentmetro.data.model.station.StationItem
import com.azamovhudstc.tashkentmetro.databinding.LineBottomsheetDialogBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class StationTimelineBottomSheet : BottomSheetDialogFragment() {

    private var _binding: LineBottomsheetDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = LineBottomsheetDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val stations: List<StationItem> = listOf(
            StartStation("Chilanzar", "Chilanzar Line", "13:49"),
            MiddleStation("Mirzo Ulugbek"),
            MiddleStation("Amir Temur"),
            EndStation("Tashkent", "Tashkent Line", "14:10")
        )


        val adapter = TimelineAdapter(stations)
        binding.routeTimelineRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.routeTimelineRecycler.adapter = adapter
//        binding.routeTimelineRecycler.addItemDecoration(
//            TimelineDecorator(
//                indicatorSize = 24f,
//                lineWidth = 15f,
//                padding = 48f,
//                position = TimelineDecorator.Position.LEFT,
//                indicatorColor = Color.parseColor("#EC5656"),
//                lineColor = Color.parseColor("#EC5656")
//
//            )
//        )
        val bottomSheetBehavior = BottomSheetBehavior.from(binding.root.parent as View)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED // or STATE_EXPANDED
        bottomSheetBehavior.peekHeight = 8000 // Set your desired peek height
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}