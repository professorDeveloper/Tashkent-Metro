package com.Zbekz.tashkentmetro.viewmodel.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.Zbekz.tashkentmetro.data.local.shp.AppReference
import com.Zbekz.tashkentmetro.data.local.shp.Language
import com.Zbekz.tashkentmetro.data.local.shp.ThemeStyle
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val appReference: AppReference
) : ViewModel() {

    private val _language = MutableLiveData<Language>()
    val language: LiveData<Language> = _language

    fun setLanguage(language: Language) {
        appReference.language = language
        _language.value = language
    }
    fun setTheme(style: ThemeStyle){
        appReference.theme = style
        appReference.applyTheme(style)
    }

    fun loadCurrentLanguage() {
        _language.value = appReference.language
    }
}
