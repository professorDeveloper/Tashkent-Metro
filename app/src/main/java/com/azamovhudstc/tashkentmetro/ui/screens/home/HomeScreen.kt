package com.azamovhudstc.tashkentmetro.ui.screens.home

import android.annotation.SuppressLint
import android.view.View
import androidx.core.content.ContextCompat
import com.azamovhudstc.tashkentmetro.R
import com.azamovhudstc.tashkentmetro.databinding.HomeScreenBinding
import com.azamovhudstc.tashkentmetro.utils.*
import com.google.android.material.appbar.AppBarLayout

class HomeScreen : BaseFragment<HomeScreenBinding>(HomeScreenBinding::inflate),
    AppBarLayout.OnOffsetChangedListener {

    private val adapter by lazy { HomeAdapter() }
    private var isCollapsed = false
    private var isSelected = false
    private val percent = 70
    private var screenWidth = 0f
    private var mMaxScrollSize = 0

    override fun onViewCreate() {
        screenWidth = resources.displayMetrics.widthPixels.toFloat()
        binding.appBarLayout.addOnOffsetChangedListener(this@HomeScreen)

        binding.textView.slideUp(900, 0)
        binding.relativeLayout.slideUp(900, 0)
        binding.relativeLayout2.slideUp(1000, 0)

        binding.trainsStatusRv.adapter = adapter
        adapter.submitList(LocalData.trainsStatusList)
    }

    @SuppressLint("ResourceType")
    override fun onOffsetChanged(appBar: AppBarLayout, i: Int) {
        if (mMaxScrollSize == 0) mMaxScrollSize = appBar.totalScrollRange
        val percentage = Math.abs(i) * 100 / mMaxScrollSize



        binding.animecover.visibility =
            if (binding.animecover.scaleX == 0f) View.GONE else View.VISIBLE
        binding.animecover.visibility =
            if (binding.animecover.scaleX == 0f) View.GONE else View.VISIBLE


        if (percentage >= percent && !isCollapsed) {
            isCollapsed = true
            requireActivity().window.statusBarColor = ContextCompat.getColor(requireActivity(), R.color.main_background)

            binding.mediaTitleContainer.slideStart(600,0)
            binding.mediaTitleContainer.visible()
        }
        if (percentage <= percent && isCollapsed) {
            isCollapsed = false
            requireActivity().window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.main_background)
            binding.mediaTitleContainer.alphaAnim()
            binding.mediaTitleContainer.gone()


        }

    }

    override fun onResume() {
        super.onResume()
        binding.mediaTitleContainer.gone()
    }
}