package com.azamovhudstc.tashkentmetro.viewmodel.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.azamovhudstc.tashkentmetro.data.model.station.Station
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(): ViewModel() {
    private val _from = MutableLiveData<Station?>()
    private val _to = MutableLiveData<Station?>()

    val fromTv: LiveData<Station?> get() =  _from
    val toTv: LiveData<Station?> get() =  _to

    private val _bothValues = MediatorLiveData<Pair<Station, Station>>().apply {
        addSource(_from) { checkBothValues() }
        addSource(_to) { checkBothValues() }
    }

    val bothValues: LiveData<Pair<Station, Station>> get() = _bothValues


    fun setFromValue(from: Station){
        _from.postValue(from)
    }

    fun setToValue(to: Station){
        _to.postValue(to)
    }

    private fun checkBothValues() {
        val fromValue = _from.value
        val toValue = _to.value
        if (fromValue != null && toValue != null) {
            _bothValues.value = Pair(fromValue, toValue)
        }else{
            _bothValues.postValue(null)
        }
    }
    fun clearFromValue(){
        _from.postValue(null)
    }
    fun clearToValue(){
        _to.postValue(null)
    }
    fun clearAllValue(){
        _from.postValue(null)
        _to.postValue(null)
        _bothValues.postValue(null)
    }

}