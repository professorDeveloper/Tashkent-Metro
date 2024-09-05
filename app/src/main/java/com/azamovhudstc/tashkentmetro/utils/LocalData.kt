package com.azamovhudstc.tashkentmetro.utils

import com.azamovhudstc.tashkentmetro.data.model.TrainStatusModel

object LocalData {

    val trainsStatusList = arrayListOf(
        TrainStatusModel(from = "Mirzo Ulug'bek", to = "Chilonzor", status = "Keldi"),
        TrainStatusModel(from = "Chilonzor", to = "Mirzo Ulug'bek", status = "Ketdi"),
        TrainStatusModel(from = "Olmazor", to = "Chilonzor", status = "Keldi")
    )
}