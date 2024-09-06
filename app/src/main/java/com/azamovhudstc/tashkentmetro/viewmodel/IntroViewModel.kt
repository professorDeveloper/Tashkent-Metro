package com.azamovhudstc.tashkentmetro.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

interface IntroViewModel {
    val loginScreenLiveData: MutableLiveData<Unit>
    val homeScreenLiveData: MutableLiveData<Unit>

}