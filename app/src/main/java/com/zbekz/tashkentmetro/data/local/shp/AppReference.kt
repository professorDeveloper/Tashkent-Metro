package com.zbekz.tashkentmetro.data.local.shp

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.zbekz.tashkentmetro.utils.enums.CurrentScreenEnum
import com.zbekz.tashkentmetro.utils.screenCurrentEnum
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

    var mapStyle:String
        get() = sharedPref.getString("mapStyle", "satellite").toString()
        set(value) {
            sharedPref.edit().putString("mapStyle", value).apply()
        }

    var token: String
        get() = sharedPref.getString("token", "null")!!
        set(value) {
            sharedPref.edit().putString("token", value.toString()).apply()
        }


    var userName: String
        get() = sharedPref.getString("user_name", "null")!!
        set(value) {
            sharedPref.edit().putString("user_name", value.toString()).apply()
        }

    var userPhone: String
        get() = sharedPref.getString("user_phone", "null")!!
        set(value) {
            sharedPref.edit().putString("user_phone", value.toString()).apply()
        }

    var language: Language
        get() {
            val code = sharedPref.getString("language", Language.ENGLISH.code)!!
            return Language.values().find { it.code == code } ?: Language.ENGLISH
        }
        set(value) {
            sharedPref.edit().putString("language", value.code).apply()
        }

    var theme: ThemeStyle
        get() {
            val themeValue = sharedPref.getString("theme", ThemeStyle.AUTO.name)
            return ThemeStyle.valueOf(themeValue ?: ThemeStyle.AUTO.name)
        }
        set(value) {
            sharedPref.edit().putString("theme", value.name).apply()
        }

    fun applyTheme(themeStyle: ThemeStyle) {
        when (themeStyle) {
            ThemeStyle.AUTO -> AppCompatDelegate.setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            )
            ThemeStyle.LIGHT -> AppCompatDelegate.setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_NO
            )
            ThemeStyle.DARK -> AppCompatDelegate.setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_YES
            )
        }
    }

}

enum class ThemeStyle {
    AUTO, LIGHT, DARK
}

interface LanguageRepository {
    fun setLanguage(language: Language)
    fun getCurrentLanguage(): Language
}

enum class Language(val code: String) {
    ENGLISH("en"),
    RUSSIAN("ru"),
    UZBEK("uz")
}