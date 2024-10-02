package com.azamovhudstc.tashkentmetro.utils

import com.azamovhudstc.tashkentmetro.data.model.TrainStatusModel
import com.azamovhudstc.tashkentmetro.data.model.station.Line
import com.azamovhudstc.tashkentmetro.data.model.station.Station
import com.azamovhudstc.tashkentmetro.data.model.station.StationLine
import com.azamovhudstc.tashkentmetro.data.model.station.StationLocation
import com.azamovhudstc.tashkentmetro.data.model.station.StationState

object LocalData {

    val trainsStatusList = arrayListOf(
        TrainStatusModel(from = "Mirzo Ulug'bek", to = "Chilonzor", status = "Keldi"),
        TrainStatusModel(from = "Chilonzor", to = "Mirzo Ulug'bek", status = "Ketdi"),
        TrainStatusModel(from = "Olmazor", to = "Chilonzor", status = "Keldi"),
        TrainStatusModel(from = "Olmazor", to = "Chilonzor", status = "Keldi"),
        TrainStatusModel(from = "Olmazor", to = "Chilonzor", status = "Keldi")
    )


    val metro: List<StationLine> = listOf(
        StationLine(
            id = 1,
            line = Line.CHILANZAR,
            stations = mutableListOf(
                Station(id = 1, line = Line.CHILANZAR, location = StationLocation(41.206413, 69.219119), name = "chinor", state = StationState.ABOVEGROUND, transferable = 50),
                Station(id = 2, line = Line.CHILANZAR, location = StationLocation(41.213493, 69.214002), name = "yangihayot", state = StationState.ABOVEGROUND),
                Station(id = 3, line = Line.CHILANZAR, location = StationLocation(41.221021, 69.208473), name = "sergeli", state = StationState.ABOVEGROUND),
                Station(id = 4, line = Line.CHILANZAR, location = StationLocation(41.227684, 69.203647), name = "uzgarish", state = StationState.ABOVEGROUND),
                Station(id = 5, line = Line.CHILANZAR, location = StationLocation(41.238478, 69.195803), name = "choshtepa", state = StationState.ABOVEGROUND),
                Station(id = 6, line = Line.CHILANZAR, location = StationLocation(41.256498, 69.195765), name = "almazar", state = StationState.UNDERGROUND),
                Station(id = 7, line = Line.CHILANZAR, location = StationLocation(41.274363, 69.204879), name = "chilanzar", state = StationState.UNDERGROUND),
                Station(id = 8, line = Line.CHILANZAR, location = StationLocation(41.282050, 69.212531), name = "mirzo_ulugbek", state = StationState.UNDERGROUND),
                Station(id = 9, line = Line.CHILANZAR, location = StationLocation(41.291957, 69.223479), name = "novza", state = StationState.UNDERGROUND),
                Station(id = 10, line = Line.CHILANZAR, location = StationLocation(41.304310, 69.235430), name = "national_park", state = StationState.UNDERGROUND),
                Station(id = 11, line = Line.CHILANZAR, location = StationLocation(41.311438, 69.242644), name = "friendship_of_peoples", state = StationState.UNDERGROUND),
                Station(id = 12, line = Line.CHILANZAR, location = StationLocation(41.317787, 69.255261), name = "pakhtakor", state = StationState.UNDERGROUND, transferable = 22),
                Station(id = 13, line = Line.CHILANZAR, location = StationLocation(41.314959, 69.270914), name = "mustakillik_square", state = StationState.UNDERGROUND),
                Station(id = 14, line = Line.CHILANZAR, location = StationLocation(41.312015, 69.281824), name = "amir_temur_square", state = StationState.UNDERGROUND, transferable = 35),
                Station(id = 15, line = Line.CHILANZAR, location = StationLocation(41.318171, 69.295758), name = "hamid_alimjan", state = StationState.UNDERGROUND),
                Station(id = 16, line = Line.CHILANZAR, location = StationLocation(41.321932, 69.311080), name = "pushkin", state = StationState.UNDERGROUND),
                Station(id = 17, line = Line.CHILANZAR, location = StationLocation(41.326108, 69.328687), name = "buyuk_ipak_yuli", state = StationState.UNDERGROUND)
            )
        ),
        StationLine(
            id = 2,
            line = Line.UZBEKISTAN,
            stations = mutableListOf(
                Station(id = 18, line = Line.UZBEKISTAN, location = StationLocation(41.345171, 69.206928), name = "beruniy", state = StationState.UNDERGROUND),
                Station(id = 19, line = Line.UZBEKISTAN, location = StationLocation(41.332341, 69.219028), name = "tinchlik", state = StationState.UNDERGROUND),
                Station(id = 20, line = Line.UZBEKISTAN, location = StationLocation(41.325867, 69.236824), name = "chorsu", state = StationState.UNDERGROUND),
                Station(id = 21, line = Line.UZBEKISTAN, location = StationLocation(41.327831, 69.246981), name = "gafur_gulam", state = StationState.UNDERGROUND),
                Station(id = 22, line = Line.UZBEKISTAN, location = StationLocation(41.321125, 69.254714), name = "alisher_navoiy", state = StationState.UNDERGROUND, transferable = 12),
                Station(id = 23, line = Line.UZBEKISTAN, location = StationLocation(41.311397, 69.253408), name = "uzbekistan", state = StationState.UNDERGROUND),
                Station(id = 24, line = Line.UZBEKISTAN, location = StationLocation(41.305022, 69.265344), name = "kosmonavtlar", state = StationState.UNDERGROUND),
                Station(id = 25, line = Line.UZBEKISTAN, location = StationLocation(41.298686, 69.273333), name = "oybek", state = StationState.UNDERGROUND, transferable = 36),
                Station(id = 26, line = Line.UZBEKISTAN, location = StationLocation(41.292136, 69.287471), name = "tashkent", state = StationState.UNDERGROUND),
                Station(id = 27, line = Line.UZBEKISTAN, location = StationLocation(41.299439, 69.303947), name = "mashinasozlar", state = StationState.UNDERGROUND),
                Station(id = 28, line = Line.UZBEKISTAN, location = StationLocation(41.293539, 69.322686), name = "dustlik", state = StationState.UNDERGROUND, transferable = 37)
            )
        ),
        StationLine(
            id = 3,
            line = Line.YUNUSOBOD,
            stations = mutableListOf(
                Station(id = 29, line = Line.YUNUSOBOD, location = StationLocation(41.377288, 69.295934), name = "turkistan", state = StationState.UNDERGROUND),
                Station(id = 30, line = Line.YUNUSOBOD, location = StationLocation(41.366611, 69.292198), name = "yunusabad", state = StationState.UNDERGROUND),
                Station(id = 31, line = Line.YUNUSOBOD, location = StationLocation(41.353020, 69.288083), name = "shakhriston", state = StationState.UNDERGROUND),
                Station(id = 32, line = Line.YUNUSOBOD, location = StationLocation(41.337080, 69.284779), name = "bodomzor", state = StationState.UNDERGROUND),
                Station(id = 33, line = Line.YUNUSOBOD, location = StationLocation(41.326844, 69.283335), name = "minor", state = StationState.UNDERGROUND),
                Station(id = 34, line = Line.YUNUSOBOD, location = StationLocation(41.321484, 69.281998), name = "abdulla_qodiriy", state = StationState.UNDERGROUND),
                Station(id = 35, line = Line.YUNUSOBOD, location = StationLocation(41.312733, 69.283470), name = "yunus_rajabiy", state = StationState.UNDERGROUND, transferable = 14),
                Station(id = 36, line = Line.YUNUSOBOD, location = StationLocation(41.298992, 69.273019), name = "ming_urik", state = StationState.UNDERGROUND, transferable = 25)
            )
        ),
        StationLine(
            id = 4,
            line = Line.INDEPENDENCEDAY,
            stations = mutableListOf(
                Station(id = 37, line = Line.INDEPENDENCEDAY, location = StationLocation(41.294528, 69.322889), name = "technopark", state = StationState.ABOVEGROUND, transferable = 28),
                Station(id = 38, line = Line.INDEPENDENCEDAY, location = StationLocation(41.297583, 69.349917), name = "yashnabad", state = StationState.ABOVEGROUND),
                Station(id = 39, line = Line.INDEPENDENCEDAY, location = StationLocation(41.292056, 69.356167), name = "tuzel", state = StationState.ABOVEGROUND),
                Station(id = 40, line = Line.INDEPENDENCEDAY, location = StationLocation(41.281972, 69.360306), name = "olmas", state = StationState.ABOVEGROUND),
                Station(id = 41, line = Line.INDEPENDENCEDAY, location = StationLocation(41.265528, 69.364917), name = "rohat", state = StationState.ABOVEGROUND),
                Station(id = 42, line = Line.INDEPENDENCEDAY, location = StationLocation(41.255472, 69.358083), name = "yangiobod", state = StationState.ABOVEGROUND),
                Station(id = 43, line = Line.INDEPENDENCEDAY, location = StationLocation(41.237417, 69.327444), name = "quyliq", state = StationState.ABOVEGROUND),
                Station(id = 44, line = Line.INDEPENDENCEDAY, location = StationLocation(41.244475, 69.308330), name = "matonat", state = StationState.ABOVEGROUND),
                Station(id = 45, line = Line.INDEPENDENCEDAY, location = StationLocation(41.244475, 69.299643), name = "qiyot", state = StationState.ABOVEGROUND),
                Station(id = 46, line = Line.INDEPENDENCEDAY, location = StationLocation(41.244515, 69.285082), name = "tolariq", state = StationState.ABOVEGROUND),
                Station(id = 47, line = Line.INDEPENDENCEDAY, location = StationLocation(41.230038, 69.270412), name = "khonobod", state = StationState.ABOVEGROUND),
                Station(id = 48, line = Line.INDEPENDENCEDAY, location = StationLocation(41.221642, 69.260495), name = "quruvchilar", state = StationState.ABOVEGROUND),
                Station(id = 49, line = Line.INDEPENDENCEDAY, location = StationLocation(41.209819, 69.231874), name = "turon", state = StationState.ABOVEGROUND),
                Station(id = 50, line = Line.INDEPENDENCEDAY, location = StationLocation(41.205300, 69.221122), name = "kipchak", state = StationState.ABOVEGROUND, transferable = 1)
            )
        )
    )
}