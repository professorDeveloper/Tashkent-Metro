package com.azamovhudstc.tashkentmetro.utils.custom

import com.azamovhudstc.tashkentmetro.data.model.station.Station
import com.azamovhudstc.tashkentmetro.data.model.station.StationLine

object StationFilter {
    fun filterStations(query: String, metroLines: List<StationLine>): List<Station> {
        val allStations = metroLines.flatMap { it.stations }

        return allStations.filter { station ->
            station.name.contains(query, ignoreCase = true)
        }
    }
}
