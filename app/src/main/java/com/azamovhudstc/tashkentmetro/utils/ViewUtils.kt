package com.azamovhudstc.tashkentmetro.utils

import android.content.Context
import android.content.res.Configuration
import com.azamovhudstc.tashkentmetro.data.local.shp.AppReference
import com.azamovhudstc.tashkentmetro.data.local.shp.Language
import java.util.Locale

object ViewUtils {
    fun setLanguage(context: Context, language: Language): Context {
        val locale = Locale(language.code)
        Locale.setDefault(locale)
        val config = context.resources.configuration
        config.setLocale(locale)
        return context.createConfigurationContext(config)
    }

    fun setLanguageForService(baseContext: Context, userPreferenceManager: AppReference) {
        val language = userPreferenceManager.language
        // Create a new locale object with the user's selected language
        val locale = Locale(language.code)

        // Set the locale for the app
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        baseContext.resources.updateConfiguration(config, baseContext.resources.displayMetrics)

    }
}