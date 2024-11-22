package com.zbekz.tashkentmetro.viewmodel.imp

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zbekz.tashkentmetro.data.local.shp.AppReference
import com.zbekz.tashkentmetro.utils.enums.CurrentScreenEnum
import com.zbekz.tashkentmetro.viewmodel.IntroViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

    @HiltViewModel
    class IntroViewModelImpl @Inject constructor(val appReference: AppReference) : IntroViewModel,
        ViewModel() {
        override val loginScreenLiveData: MutableLiveData<Unit> = MutableLiveData()
        override val homeScreenLiveData: MutableLiveData<Unit> = MutableLiveData()

        fun checkPage() {

            when (appReference.currentScreenEnum) {
                CurrentScreenEnum.HOME -> {
                    homeScreenLiveData.postValue(Unit)
                }
                CurrentScreenEnum.INTRO -> {
                    loginScreenLiveData.postValue(Unit)
                }
            }
        }


}