package com.azamovhudstc.tashkentmetro.ui.screens.home

import com.azamovhudstc.tashkentmetro.databinding.HomeScreenBinding
import com.azamovhudstc.tashkentmetro.utils.BaseFragment
import com.azamovhudstc.tashkentmetro.utils.LocalData
import com.azamovhudstc.tashkentmetro.utils.slideStart
import com.azamovhudstc.tashkentmetro.utils.slideUp

class HomeScreen : BaseFragment<HomeScreenBinding>(HomeScreenBinding::inflate)
     {

    private val adapter by lazy { HomeAdapter() }
    private var screenWidth = 0f

    override fun onViewCreate() {
        screenWidth = resources.displayMetrics.widthPixels.toFloat()

        binding.textView.slideUp(900, 0)
        binding.relativeLayout.slideUp(900, 0)
        binding.relativeLayout2.slideUp(1000, 0)
        binding.train1.slideStart(900,0)
        binding.train2.slideStart(900,0)
        binding.trainsStatusRv.adapter = adapter
        adapter.submitList(LocalData.trainsStatusList)
    }


}