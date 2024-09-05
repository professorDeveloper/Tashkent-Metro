package com.azamovhudstc.tashkentmetro.ui.screens.home

import com.azamovhudstc.tashkentmetro.databinding.HomeScreenBinding
import com.azamovhudstc.tashkentmetro.utils.BaseFragment
import com.azamovhudstc.tashkentmetro.utils.LocalData

class HomeScreen : BaseFragment<HomeScreenBinding>(HomeScreenBinding::inflate) {

    private val adapter by lazy { HomeAdapter() }


    override fun onViewCreate() {

        binding.trainsStatusRv.adapter = adapter
        adapter.submitList(LocalData.trainsStatusList)

    }

}