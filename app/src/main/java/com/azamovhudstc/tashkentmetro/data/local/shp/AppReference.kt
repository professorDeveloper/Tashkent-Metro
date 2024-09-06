package com.azamovhudstc.tashkentmetro.data.local.shp

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.azamovhudstc.infinityinsurance.utils.enums.CurrentScreenEnum
import com.azamovhudstc.tashkentmetro.utils.screenCurrentEnum
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppReference @Inject constructor(
    @ApplicationContext
    context: Context,
) {
    private var sharedPref: SharedPreferences =
        context.getSharedPreferences("metr_shp", MODE_PRIVATE)
    private var editor: SharedPreferences.Editor = sharedPref.edit()




    var currentScreenEnum: CurrentScreenEnum
        get() = sharedPref.getString("current_screen", CurrentScreenEnum.INTRO.name)!!
            .screenCurrentEnum()
        set(value) {
            sharedPref.edit().putString("current_screen", value.name).apply()
        }

    var token: String
        get() = sharedPref.getString("token", "null")!!
        set(value) {
            sharedPref.edit().putString("token", value.toString()).apply()
        }



}