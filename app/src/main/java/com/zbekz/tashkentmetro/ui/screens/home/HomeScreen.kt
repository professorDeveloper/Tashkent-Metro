package com.zbekz.tashkentmetro.ui.screens.home

import android.os.Bundle
import com.Zbekz.tashkentmetro.databinding.HomeScreenBinding
import com.zbekz.tashkentmetro.utils.BaseFragment
import com.zbekz.tashkentmetro.utils.LocalData

class HomeScreen : BaseFragment<HomeScreenBinding>(HomeScreenBinding::inflate)
     {

    private val adapter by lazy { HomeAdapter() }
    private var screenWidth = 0f

    override fun onViewCreate(savedInstanceState: Bundle?) {
        screenWidth = resources.displayMetrics.widthPixels.toFloat()

        binding.trainsStatusRv.adapter = adapter
        adapter.submitList(LocalData.trainsStatusList)
    }


}