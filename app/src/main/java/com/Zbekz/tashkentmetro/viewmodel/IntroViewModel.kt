package com.Zbekz.tashkentmetro.viewmodel

import androidx.lifecycle.MutableLiveData

interface IntroViewModel {
    val loginScreenLiveData: MutableLiveData<Unit>
    val homeScreenLiveData: MutableLiveData<Unit>

}