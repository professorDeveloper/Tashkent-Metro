package com.azamovhudstc.tashkentmetro.viewmodel.imp

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.azamovhudstc.infinityinsurance.utils.enums.CurrentScreenEnum
import com.azamovhudstc.tashkentmetro.data.local.shp.AppReference
import com.azamovhudstc.tashkentmetro.viewmodel.IntroViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class IntroViewModelImpl @Inject constructor(val appReference: AppReference) : IntroViewModel,
    ViewModel() {
    override val loginScreenLiveData: MutableLiveData<Unit> = MutableLiveData()
    override val homeScreenLiveData: MutableLiveData<Unit> = MutableLiveData()

    init {

    }

    fun checkPage(){
        when (appReference.currentScreenEnum) {
            CurrentScreenEnum.HOME -> {
                homeScreenLiveData.postValue(Unit)
            }

            CurrentScreenEnum.INTRO -> loginScreenLiveData.postValue(Unit)
        }
    }


}