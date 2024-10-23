package com.zbekz.tashkentmetro.data.model.station

data class StationLang(
    val id: String,
    val translations: Map<String, String> // Language code to translation map
)
