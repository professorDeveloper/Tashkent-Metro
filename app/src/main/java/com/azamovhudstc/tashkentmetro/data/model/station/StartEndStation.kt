package com.azamovhudstc.tashkentmetro.data.model.station

data class StartStation(
   override val name: String,
   override   val line: String,
    val time: String // The time for start stations
) : StationItem

// Data class for the end station
data class EndStation(
    override  val name: String,
    override  val line: String,
    val time: String // The time for end stations
) : StationItem

// Data class for the middle station
data class MiddleStation(
    override    val name: String
) : StationItem {
    override val line: String
        get() = ""

}
