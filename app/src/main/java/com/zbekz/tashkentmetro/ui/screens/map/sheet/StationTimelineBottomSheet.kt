package com.zbekz.tashkentmetro.ui.screens.map.sheet

import TimelineAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.zbekz.tashkentmetro.data.model.station.EndStation
import com.zbekz.tashkentmetro.data.model.station.Line
import com.zbekz.tashkentmetro.data.model.station.MiddleStation
import com.zbekz.tashkentmetro.data.model.station.StartStation
import com.zbekz.tashkentmetro.data.model.station.StationItem
import com.zbekz.tashkentmetro.data.model.station.StationLine
import com.zbekz.tashkentmetro.databinding.LineBottomsheetDialogBinding
import com.zbekz.tashkentmetro.utils.formatString
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


class StationTimelineBottomSheet(val result: MutableList<StationLine>) : BottomSheetDialogFragment() {

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
        search()

        val bottomSheetBehavior = BottomSheetBehavior.from(binding.root.parent as View)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED // or STATE_EXPANDED
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun search(){
        val a = splitStationLinesByLine(result)
        val lineTimeList = mapToStationItems(a)
        val adapter = TimelineAdapter(lineTimeList)

        binding.routeTimelineRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.routeTimelineRecycler.adapter = adapter

    }
    private fun mapToStationItems(map: Map<Line, List<StationLine>>): List<StationItem> {
        val stationItems = mutableListOf<StationItem>()

        var firstTime = true
        var endTime = true

        map.forEach { (line, stationLines) ->
            stationLines.forEach { stationLine ->
                stationLine.stations.forEachIndexed { index, station ->
                    when (index) {
                        0 -> {
                            stationItems.add(
                                StartStation(
                                    name = formatString(station.name,requireContext()),
                                    line = line.name,
                                    time = if (firstTime) getCurrentTime() else getTimeAfterMinutes(22)
                                )
                            )
                            firstTime = false
                        }
                        stationLine.stations.size - 1 -> {
                            stationItems.add(
                                EndStation(
                                    name = formatString(station.name,requireContext()),
                                    line = line.name,
                                    time = if (endTime) getTimeAfterMinutes(20) else getTimeAfterMinutes(42)
                                )
                            )
                            endTime = false
                        }
                        else -> {
                            stationItems.add(
                                MiddleStation(
                                    name = formatString(station.name,requireContext()),
                                    line = line.name
                                )
                            )
                        }
                    }
                }
            }
        }

        return stationItems
    }

    private fun getCurrentTime(): String {
        val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        return dateFormat.format(Date())
    }



    private fun getTimeAfterMinutes(minutes: Int): String {

        val calendar = Calendar.getInstance()
        calendar.add(Calendar.MINUTE, minutes)

        val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }



    fun splitStationLinesByLine(stationLines: List<StationLine>): Map<Line, List<StationLine>> {
        val result = mutableMapOf<Line, MutableList<StationLine>>()

        stationLines.forEach { stationLine ->
            val line = stationLine.line
            if (result.containsKey(line)) {
                result[line]?.add(stationLine)
            } else {
                result[line] = mutableListOf(stationLine)
            }
        }

        return result
    }

    override fun onStart() {
        super.onStart()

        dialog?.let { dialog ->
            val bottomSheet = dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.layoutParams?.height = (resources.displayMetrics.heightPixels * 0.9).toInt()
        }
    }

}