package com.Zbekz.tashkentmetro.ui.screens.home

import com.Zbekz.tashkentmetro.databinding.HomeScreenBinding
import com.Zbekz.tashkentmetro.utils.BaseFragment
import com.Zbekz.tashkentmetro.utils.LocalData

class HomeScreen : BaseFragment<HomeScreenBinding>(HomeScreenBinding::inflate)
     {

    private val adapter by lazy { HomeAdapter() }
    private var screenWidth = 0f

    override fun onViewCreate() {
        screenWidth = resources.displayMetrics.widthPixels.toFloat()

        binding.trainsStatusRv.adapter = adapter
        adapter.submitList(LocalData.trainsStatusList)
    }


}