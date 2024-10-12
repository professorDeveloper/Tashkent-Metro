package com.zbekz.tashkentmetro.utils

import com.zbekz.tashkentmetro.data.model.TrainStatusModel
import com.zbekz.tashkentmetro.data.model.station.Line
import com.zbekz.tashkentmetro.data.model.station.Station
import com.zbekz.tashkentmetro.data.model.station.StationLang
import com.zbekz.tashkentmetro.data.model.station.StationLine
import com.zbekz.tashkentmetro.data.model.station.StationLocation
import com.zbekz.tashkentmetro.data.model.station.StationState

object LocalData {

    val trainsStatusList = arrayListOf(
        TrainStatusModel(from = "Mirzo Ulug'bek", to = "Chilonzor", status = "Keldi"),
        TrainStatusModel(from = "Chilonzor", to = "Mirzo Ulug'bek", status = "Ketdi"),
        TrainStatusModel(from = "Olmazor", to = "Chilonzor", status = "Keldi"),
        TrainStatusModel(from = "Olmazor", to = "Chilonzor", status = "Keldi"),
        TrainStatusModel(from = "Olmazor", to = "Chilonzor", status = "Keldi")
    )

    val stations = listOf(
        StationLang("abdulla_qodiriy", mapOf("en" to "Abdulla Qodiriy", "ru" to "Абдулла Кодирий", "uz" to "Abdulla Qodiriy")),
        StationLang("alisher_navoiy", mapOf("en" to "Alisher Navoiy", "ru" to "Алишер Навоий", "uz" to "Alisher Navoiy")),
        StationLang("almazar", mapOf("en" to "Almazar", "ru" to "Алмазар", "uz" to "Olmazor")),
        StationLang("amir_temur_square", mapOf("en" to "Amir Temur Square", "ru" to "Сквер Амира Темура", "uz" to "Amir Temur Maydoni")),
        StationLang("beruniy", mapOf("en" to "Beruniy", "ru" to "Беруни", "uz" to "Beruniy")),
        StationLang("bodomzor", mapOf("en" to "Bodomzor", "ru" to "Бодомзор", "uz" to "Bodomzor")),
        StationLang("buyuk_ipak_yuli", mapOf("en" to "Buyuk Ipak Yuli", "ru" to "Буюк ​​ипак йули", "uz" to "Buyuk Ipak Yo’li")),
        StationLang("chilanzar", mapOf("en" to "Chilanzar", "ru" to "Чиланзар", "uz" to "Chilonzor")),
        StationLang("chinor", mapOf("en" to "Chinor", "ru" to "Чинор", "uz" to "Chinor")),
        StationLang("chorsu", mapOf("en" to "Chorsu", "ru" to "Чорсу", "uz" to "Chorsu")),
        StationLang("choshtepa", mapOf("en" to "Choshtepa", "ru" to "Чоштепа", "uz" to "Choshtepa")),
        StationLang("dustlik", mapOf("en" to "Dustlik", "ru" to "Дустлик", "uz" to "Do’stlik")),
        StationLang("friendship_of_peoples", mapOf("en" to "Friendship of Peoples", "ru" to "Дружба народов", "uz" to "Xalqlar Do’stligi")),
        StationLang("gafur_gulam", mapOf("en" to "Gafur Gulam", "ru" to "Гафур Гулям", "uz" to "G’afur G’ulom")),
        StationLang("hamid_alimjan", mapOf("en" to "Hamid Alimjan", "ru" to "Хамид Алимджан", "uz" to "Hamid Alimjon")),
        StationLang("khonobod", mapOf("en" to "Khonobod", "ru" to "Хонобод", "uz" to "Xonobod")),
        StationLang("kipchak", mapOf("en" to "Kipchak", "ru" to "Кипчак", "uz" to "Qipchoq")),
        StationLang("kosmonavtlar", mapOf("en" to "Kosmonavtlar", "ru" to "Космонавты", "uz" to "Kosmonavtlar")),
        StationLang("marhamat", mapOf("en" to "Marhamat", "ru" to "Марҳамат", "uz" to "Marhamat")),
        StationLang("mashinasozlar", mapOf("en" to "Mashinasozlar", "ru" to "Машинасозлар", "uz" to "Mashinasozlar")),
        StationLang("matonat", mapOf("en" to "Matonat", "ru" to "Матонат", "uz" to "Matonat")),
        StationLang("ming_urik", mapOf("en" to "Ming Urik", "ru" to "Минг Урик", "uz" to "Ming O’rik")),
        StationLang("minor", mapOf("en" to "Minor", "ru" to "Минор", "uz" to "Minor")),
        StationLang("mirzo_ulugbek", mapOf("en" to "Mirzo Ulugbek", "ru" to "Мирзо Улугбек", "uz" to "Mirzo Ulug’bek")),
        StationLang("mustakillik_square", mapOf("en" to "Mustakillik Square", "ru" to "Площадь Мустакиллик", "uz" to "Mustaqillik Maydoni")),
        StationLang("national_park", mapOf("en" to "National Park", "ru" to "Национальный парк", "uz" to "Milliy bog’")),
        StationLang("novza", mapOf("en" to "Novza", "ru" to "Новза", "uz" to "Novza")),
        StationLang("olmas", mapOf("en" to "Olmas", "ru" to "Алмас", "uz" to "Olmas")),
        StationLang("oybek", mapOf("en" to "Oybek", "ru" to "Ойбек", "uz" to "Oybek")),
        StationLang("pakhtakor", mapOf("en" to "Pakhtakor", "ru" to "Пахтакор", "uz" to "Paxtakor")),
        StationLang("pushkin", mapOf("en" to "Pushkin", "ru" to "Пушкин", "uz" to "Pushkin")),
        StationLang("qiyot", mapOf("en" to "Qiyot", "ru" to "Киёт", "uz" to "Qiyot")),
        StationLang("quruvchilar", mapOf("en" to "Quruvchilar", "ru" to "Курувчилар", "uz" to "Quruvchilar")),
        StationLang("quyliq", mapOf("en" to "Quyliq", "ru" to "Куилик", "uz" to "Qo’yliq")),
        StationLang("rohat", mapOf("en" to "Rohat", "ru" to "Рохат", "uz" to "Rohat")),
        StationLang("sergeli", mapOf("en" to "Sergeli", "ru" to "Сергели", "uz" to "Sergeli")),
        StationLang("shakhriston", mapOf("en" to "Shakhriston", "ru" to "Шахристон", "uz" to "Shahriston")),
        StationLang("tashkent", mapOf("en" to "Tashkent", "ru" to "Ташкент", "uz" to "Toshkent")),
        StationLang("technopark", mapOf("en" to "Technopark", "ru" to "Технопарк", "uz" to "Texnopark")),
        StationLang("temiryo'l", mapOf("en" to "Temiryo'l", "ru" to "Темирйул", "uz" to "Temiryo’l")),
        StationLang("tinchlik", mapOf("en" to "Tinchlik", "ru" to "Тинчлик", "uz" to "Tinchlik")),
        StationLang("tolariq", mapOf("en" to "Tolariq", "ru" to "Толарик", "uz" to "Tolariq")),
        StationLang("turkistan", mapOf("en" to "Turkistan", "ru" to "Туркистан", "uz" to "Turkistan")),
        StationLang("turon", mapOf("en" to "Turon", "ru" to "Турон", "uz" to "Turon")),
        StationLang("tuzel", mapOf("en" to "Tuzel", "ru" to "Тузель", "uz" to "Tuzel")),
        StationLang("uzbekistan", mapOf("en" to "Uzbekistan", "ru" to "Узбекистан", "uz" to "O’zbekiston")),
        StationLang("uzgarish", mapOf("en" to "Uzgarish", "ru" to "Узгариш", "uz" to "O’zgarish")),
        StationLang("yangihayot", mapOf("en" to "Yangihayot", "ru" to "Янгиҳайот", "uz" to "Yangihayot")),
        StationLang("yangiobod", mapOf("en" to "Yangiobod", "ru" to "Янгиобод", "uz" to "Yangiobod")),
        StationLang("yashnabad", mapOf("en" to "Yashnabad", "ru" to "Яшнабад", "uz" to "Yashnobod")),
        StationLang("yunus_rajabiy", mapOf("en" to "Yunus Rajabiy", "ru" to "Юнус Раджабий", "uz" to "Yunus Rajabiy")),
        StationLang("yunusabad", mapOf("en" to "Yunusabad", "ru" to "Юнусабад", "uz" to "Yunusobod"))
    )


    val popularStations = listOf(
        Station(
            id = 7,
            line = Line.CHILANZAR,
            location = StationLocation(41.274363, 69.204879),
            name = "chilanzar",
            state = StationState.UNDERGROUND
        ),
        Station(
            id = 11,
            line = Line.CHILANZAR,
            location = StationLocation(41.311438, 69.242644),
            name = "friendship_of_peoples",
            state = StationState.UNDERGROUND
        ),
        Station(
            id = 12,
            line = Line.CHILANZAR,
            location = StationLocation(41.317787, 69.255261),
            name = "pakhtakor",
            state = StationState.UNDERGROUND,
            transferable = 22
        ),
        Station(
            id = 14,
            line = Line.CHILANZAR,
            location = StationLocation(41.312015, 69.281824),
            name = "amir_temur_square",
            state = StationState.UNDERGROUND,
            transferable = 35
        ),
        Station(
            id = 22,
            line = Line.UZBEKISTAN,
            location = StationLocation(41.321125, 69.254714),
            name = "alisher_navoiy",
            state = StationState.UNDERGROUND,
            transferable = 12
        ),
        Station(
            id = 25,
            line = Line.UZBEKISTAN,
            location = StationLocation(41.298686, 69.273333),
            name = "oybek",
            state = StationState.UNDERGROUND,
            transferable = 36
        ),
        Station(
            id = 35,
            line = Line.YUNUSOBOD,
            location = StationLocation(41.312733, 69.283470),
            name = "yunus_rajabiy",
            state = StationState.UNDERGROUND,
            transferable = 14
        ),
        Station(
            id = 36,
            line = Line.YUNUSOBOD,
            location = StationLocation(41.298992, 69.273019),
            name = "ming_urik",
            state = StationState.UNDERGROUND,
            transferable = 25
        )

    )

    val metro: List<StationLine> = listOf(
        StationLine(
            id = 1,
            line = Line.CHILANZAR,
            stations = mutableListOf(
                Station(
                    id = 1,
                    line = Line.CHILANZAR,
                    location = StationLocation(41.206413, 69.219119),
                    name = "chinor",
                    state = StationState.ABOVEGROUND,
                    transferable = 50
                ),
                Station(
                    id = 2,
                    line = Line.CHILANZAR,
                    location = StationLocation(41.213493, 69.214002),
                    name = "yangihayot",
                    state = StationState.ABOVEGROUND
                ),
                Station(
                    id = 3,
                    line = Line.CHILANZAR,
                    location = StationLocation(41.221021, 69.208473),
                    name = "sergeli",
                    state = StationState.ABOVEGROUND
                ),
                Station(
                    id = 4,
                    line = Line.CHILANZAR,
                    location = StationLocation(41.227684, 69.203647),
                    name = "uzgarish",
                    state = StationState.ABOVEGROUND
                ),
                Station(
                    id = 5,
                    line = Line.CHILANZAR,
                    location = StationLocation(41.238478, 69.195803),
                    name = "choshtepa",
                    state = StationState.ABOVEGROUND
                ),
                Station(
                    id = 6,
                    line = Line.CHILANZAR,
                    location = StationLocation(41.256498, 69.195765),
                    name = "almazar",
                    state = StationState.UNDERGROUND
                ),
                Station(
                    id = 7,
                    line = Line.CHILANZAR,
                    location = StationLocation(41.274363, 69.204879),
                    name = "chilanzar",
                    state = StationState.UNDERGROUND
                ),
                Station(
                    id = 8,
                    line = Line.CHILANZAR,
                    location = StationLocation(41.282050, 69.212531),
                    name = "mirzo_ulugbek",
                    state = StationState.UNDERGROUND
                ),
                Station(
                    id = 9,
                    line = Line.CHILANZAR,
                    location = StationLocation(41.291957, 69.223479),
                    name = "novza",
                    state = StationState.UNDERGROUND
                ),
                Station(
                    id = 10,
                    line = Line.CHILANZAR,
                    location = StationLocation(41.304310, 69.235430),
                    name = "national_park",
                    state = StationState.UNDERGROUND
                ),
                Station(
                    id = 11,
                    line = Line.CHILANZAR,
                    location = StationLocation(41.311438, 69.242644),
                    name = "friendship_of_peoples",
                    state = StationState.UNDERGROUND
                ),
                Station(
                    id = 12,
                    line = Line.CHILANZAR,
                    location = StationLocation(41.317787, 69.255261),
                    name = "pakhtakor",
                    state = StationState.UNDERGROUND,
                    transferable = 22
                ),
                Station(
                    id = 13,
                    line = Line.CHILANZAR,
                    location = StationLocation(41.314959, 69.270914),
                    name = "mustakillik_square",
                    state = StationState.UNDERGROUND
                ),
                Station(
                    id = 14,
                    line = Line.CHILANZAR,
                    location = StationLocation(41.312015, 69.281824),
                    name = "amir_temur_square",
                    state = StationState.UNDERGROUND,
                    transferable = 35
                ),
                Station(
                    id = 15,
                    line = Line.CHILANZAR,
                    location = StationLocation(41.318171, 69.295758),
                    name = "hamid_alimjan",
                    state = StationState.UNDERGROUND
                ),
                Station(
                    id = 16,
                    line = Line.CHILANZAR,
                    location = StationLocation(41.321932, 69.311080),
                    name = "pushkin",
                    state = StationState.UNDERGROUND
                ),
                Station(
                    id = 17,
                    line = Line.CHILANZAR,
                    location = StationLocation(41.326108, 69.328687),
                    name = "buyuk_ipak_yuli",
                    state = StationState.UNDERGROUND
                )
            )
        ),
        StationLine(
            id = 2,
            line = Line.UZBEKISTAN,
            stations = mutableListOf(
                Station(
                    id = 18,
                    line = Line.UZBEKISTAN,
                    location = StationLocation(41.345171, 69.206928),
                    name = "beruniy",
                    state = StationState.UNDERGROUND
                ),
                Station(
                    id = 19,
                    line = Line.UZBEKISTAN,
                    location = StationLocation(41.332341, 69.219028),
                    name = "tinchlik",
                    state = StationState.UNDERGROUND
                ),
                Station(
                    id = 20,
                    line = Line.UZBEKISTAN,
                    location = StationLocation(41.325867, 69.236824),
                    name = "chorsu",
                    state = StationState.UNDERGROUND
                ),
                Station(
                    id = 21,
                    line = Line.UZBEKISTAN,
                    location = StationLocation(41.327831, 69.246981),
                    name = "gafur_gulam",
                    state = StationState.UNDERGROUND
                ),
                Station(
                    id = 22,
                    line = Line.UZBEKISTAN,
                    location = StationLocation(41.321125, 69.254714),
                    name = "alisher_navoiy",
                    state = StationState.UNDERGROUND,
                    transferable = 12
                ),
                Station(
                    id = 23,
                    line = Line.UZBEKISTAN,
                    location = StationLocation(41.311397, 69.253408),
                    name = "uzbekistan",
                    state = StationState.UNDERGROUND
                ),
                Station(
                    id = 24,
                    line = Line.UZBEKISTAN,
                    location = StationLocation(41.305022, 69.265344),
                    name = "kosmonavtlar",
                    state = StationState.UNDERGROUND
                ),
                Station(
                    id = 25,
                    line = Line.UZBEKISTAN,
                    location = StationLocation(41.298686, 69.273333),
                    name = "oybek",
                    state = StationState.UNDERGROUND,
                    transferable = 36
                ),
                Station(
                    id = 26,
                    line = Line.UZBEKISTAN,
                    location = StationLocation(41.292136, 69.287471),
                    name = "tashkent",
                    state = StationState.UNDERGROUND
                ),
                Station(
                    id = 27,
                    line = Line.UZBEKISTAN,
                    location = StationLocation(41.299439, 69.303947),
                    name = "mashinasozlar",
                    state = StationState.UNDERGROUND
                ),
                Station(
                    id = 28,
                    line = Line.UZBEKISTAN,
                    location = StationLocation(41.293539, 69.322686),
                    name = "dustlik",
                    state = StationState.UNDERGROUND,
                    transferable = 37
                )
            )
        ),
        StationLine(
            id = 3,
            line = Line.YUNUSOBOD,
            stations = mutableListOf(
                Station(
                    id = 29,
                    line = Line.YUNUSOBOD,
                    location = StationLocation(41.377288, 69.295934),
                    name = "turkistan",
                    state = StationState.UNDERGROUND
                ),
                Station(
                    id = 30,
                    line = Line.YUNUSOBOD,
                    location = StationLocation(41.366611, 69.292198),
                    name = "yunusabad",
                    state = StationState.UNDERGROUND
                ),
                Station(
                    id = 31,
                    line = Line.YUNUSOBOD,
                    location = StationLocation(41.353020, 69.288083),
                    name = "shakhriston",
                    state = StationState.UNDERGROUND
                ),
                Station(
                    id = 32,
                    line = Line.YUNUSOBOD,
                    location = StationLocation(41.337080, 69.284779),
                    name = "bodomzor",
                    state = StationState.UNDERGROUND
                ),
                Station(
                    id = 33,
                    line = Line.YUNUSOBOD,
                    location = StationLocation(41.326844, 69.283335),
                    name = "minor",
                    state = StationState.UNDERGROUND
                ),
                Station(
                    id = 34,
                    line = Line.YUNUSOBOD,
                    location = StationLocation(41.321484, 69.281998),
                    name = "abdulla_qodiriy",
                    state = StationState.UNDERGROUND
                ),
                Station(
                    id = 35,
                    line = Line.YUNUSOBOD,
                    location = StationLocation(41.312733, 69.283470),
                    name = "yunus_rajabiy",
                    state = StationState.UNDERGROUND,
                    transferable = 14
                ),
                Station(
                    id = 36,
                    line = Line.YUNUSOBOD,
                    location = StationLocation(41.298992, 69.273019),
                    name = "ming_urik",
                    state = StationState.UNDERGROUND,
                    transferable = 25
                )
            )
        ),
        StationLine(
            id = 4,
            line = Line.INDEPENDENCEDAY,
            stations = mutableListOf(
                Station(
                    id = 37,
                    line = Line.INDEPENDENCEDAY,
                    location = StationLocation(41.294528, 69.322889),
                    name = "technopark",
                    state = StationState.ABOVEGROUND,
                    transferable = 28
                ),
                Station(
                    id = 38,
                    line = Line.INDEPENDENCEDAY,
                    location = StationLocation(41.297583, 69.349917),
                    name = "yashnabad",
                    state = StationState.ABOVEGROUND
                ),
                Station(
                    id = 39,
                    line = Line.INDEPENDENCEDAY,
                    location = StationLocation(41.292056, 69.356167),
                    name = "tuzel",
                    state = StationState.ABOVEGROUND
                ),
                Station(
                    id = 40,
                    line = Line.INDEPENDENCEDAY,
                    location = StationLocation(41.281972, 69.360306),
                    name = "olmas",
                    state = StationState.ABOVEGROUND
                ),
                Station(
                    id = 41,
                    line = Line.INDEPENDENCEDAY,
                    location = StationLocation(41.265528, 69.364917),
                    name = "rohat",
                    state = StationState.ABOVEGROUND
                ),
                Station(
                    id = 42,
                    line = Line.INDEPENDENCEDAY,
                    location = StationLocation(41.255472, 69.358083),
                    name = "yangiobod",
                    state = StationState.ABOVEGROUND
                ),
                Station(
                    id = 43,
                    line = Line.INDEPENDENCEDAY,
                    location = StationLocation(41.237417, 69.327444),
                    name = "quyliq",
                    state = StationState.ABOVEGROUND
                ),
                Station(
                    id = 44,
                    line = Line.INDEPENDENCEDAY,
                    location = StationLocation(41.244475, 69.308330),
                    name = "matonat",
                    state = StationState.ABOVEGROUND
                ),
                Station(
                    id = 45,
                    line = Line.INDEPENDENCEDAY,
                    location = StationLocation(41.244475, 69.299643),
                    name = "qiyot",
                    state = StationState.ABOVEGROUND
                ),
                Station(
                    id = 46,
                    line = Line.INDEPENDENCEDAY,
                    location = StationLocation(41.244515, 69.285082),
                    name = "tolariq",
                    state = StationState.ABOVEGROUND
                ),
                Station(
                    id = 47,
                    line = Line.INDEPENDENCEDAY,
                    location = StationLocation(41.230038, 69.270412),
                    name = "khonobod",
                    state = StationState.ABOVEGROUND
                ),
                Station(
                    id = 48,
                    line = Line.INDEPENDENCEDAY,
                    location = StationLocation(41.221642, 69.260495),
                    name = "quruvchilar",
                    state = StationState.ABOVEGROUND
                ),
                Station(
                    id = 49,
                    line = Line.INDEPENDENCEDAY,
                    location = StationLocation(41.209819, 69.231874),
                    name = "turon",
                    state = StationState.ABOVEGROUND
                ),
                Station(
                    id = 50,
                    line = Line.INDEPENDENCEDAY,
                    location = StationLocation(41.205300, 69.221122),
                    name = "kipchak",
                    state = StationState.ABOVEGROUND,
                    transferable = 1
                )
            )
        )
    )
}