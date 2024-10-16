package com.zbekz.tashkentmetro.data.model.station

import android.content.Context
import com.zbekz.tashkentmetro.data.local.shp.AppReference
import com.zbekz.tashkentmetro.data.local.shp.Language

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

fun localizeStationState(stationState: StationState,context: Context):String{
    val appReference =AppReference(context)
    when(appReference.language){
        Language.ENGLISH->{
            return when(stationState){
                StationState.ABOVEGROUND->"Aboveground"
                StationState.UNDERGROUND->"Underground"
            }
        }
        Language.RUSSIAN ->{
            return when(stationState){
                StationState.ABOVEGROUND->"Подземная"
                StationState.UNDERGROUND->"Надземная"
            }
        }
        Language.UZBEK ->{
            return when(stationState){
                StationState.ABOVEGROUND->"Yer usti"
                StationState.UNDERGROUND->"Yer osti"
            }
        }
    }
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
