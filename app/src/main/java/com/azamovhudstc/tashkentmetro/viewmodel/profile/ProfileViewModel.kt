package com.azamovhudstc.tashkentmetro.viewmodel.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.azamovhudstc.tashkentmetro.data.local.shp.AppReference
import com.azamovhudstc.tashkentmetro.data.local.shp.Language
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

    fun loadCurrentLanguage() {
        _language.value = appReference.language
    }
}
