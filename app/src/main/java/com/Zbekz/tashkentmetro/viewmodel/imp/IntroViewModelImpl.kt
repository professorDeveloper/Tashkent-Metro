package com.Zbekz.tashkentmetro.viewmodel.imp

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.Zbekz.tashkentmetro.utils.enums.CurrentScreenEnum
import com.Zbekz.tashkentmetro.data.local.shp.AppReference
import com.Zbekz.tashkentmetro.viewmodel.IntroViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

    @HiltViewModel
    class IntroViewModelImpl @Inject constructor(val appReference: AppReference) : IntroViewModel,
        ViewModel() {
        override val loginScreenLiveData: MutableLiveData<Unit> = MutableLiveData()
        override val homeScreenLiveData: MutableLiveData<Unit> = MutableLiveData()

    fun checkPage(){
        when (appReference.currentScreenEnum) {
            CurrentScreenEnum.HOME -> {
                homeScreenLiveData.postValue(Unit)
            }

            CurrentScreenEnum.INTRO -> loginScreenLiveData.postValue(Unit)
        }
    }


}