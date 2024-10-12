package com.zbekz.tashkentmetro.utils.custom

import com.zbekz.tashkentmetro.data.model.station.Station
import com.zbekz.tashkentmetro.data.model.station.StationLang
import com.zbekz.tashkentmetro.data.model.station.StationLine
import com.zbekz.tashkentmetro.utils.LocalData.stations

object StationFilter {
    fun filterStations(query: String, metroLines: List<StationLine>): List<Station> {
        val stationLangFormat = searchStation(query)
        val allStations = metroLines.flatMap { it.stations }
        val newResult = ArrayList<Station>()
        for (id in stationLangFormat) {
            val matchingName = allStations.find { it.name == id.id }
            if (matchingName != null) {
                newResult.add(matchingName)
            }
        }
        return newResult
    }

   private fun searchStation(query: String): List<StationLang> {
        return stations.filter { station ->
            station.translations.any { (_, translation) ->
                translation.contains(query, ignoreCase = true)
            }
        }
    }

}
