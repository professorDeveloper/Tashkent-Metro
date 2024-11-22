package com.zbekz.tashkentmetro.data.local.shp

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppReference @Inject constructor(
    @ApplicationContext
    context: Context,
) {

    private val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)



    fun setCurrentScreen(value:String){
        sharedPref.edit().putString("current_screen", value).apply()
    }

    fun getCurrentScreen():String?{
      return  sharedPref.getString("current_screen", "intro")
    }


    var mapStyle:String
        get() = sharedPref.getString("mapStyle", "normal").toString()
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
            val code = sharedPref.getString("language", Language.UZBEK.code)!!
            return Language.values().find { it.code == code } ?: Language.UZBEK
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