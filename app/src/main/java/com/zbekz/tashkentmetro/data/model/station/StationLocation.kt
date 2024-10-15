package com.zbekz.tashkentmetro.data.model.station

data class StationLocation(
    val latitude: Double,
    val longitude: Double
)

enum class Line {
    CHILANZAR, UZBEKISTAN, YUNUSABAD, INDEPENDENCEDAY
}

enum class StationState {
    ABOVEGROUND, UNDERGROUND
}

data class Station(
    val id: Int,
    val line: Line,
    val location: StationLocation,
    val name: String,
    val state: StationState,
    val transferable: Int? = null
)

data class StationLine(
    val id: Int,
    val line: Line,
    val stations: MutableList<Station>
)
