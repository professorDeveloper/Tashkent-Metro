package com.Zbekz.tashkentmetro.domain.preference

//import android.content.Context
//import androidx.security.crypto.EncryptedSharedPreferences
//import androidx.security.crypto.MasterKey
//
//class UserPreferenceManager(context: Context) {
//
//    private val masterKey = MasterKey.Builder(context)
//        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
//        .build()
//
//    private val prefs = EncryptedSharedPreferences.create(
//        context,
//        "secure_prefs",
//        masterKey,
//        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
//        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
//    )
//
//    fun isSeenIntro(){
//        prefs.edit().putBoolean("intro", true).apply()
//    }
//
//    fun getSeenIntro(): Boolean = prefs.getBoolean("intro",false)
//}