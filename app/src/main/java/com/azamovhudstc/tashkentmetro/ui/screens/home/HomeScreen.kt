package com.azamovhudstc.tashkentmetro.ui.screens.home

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.res.Resources
import android.util.TypedValue
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import androidx.core.math.MathUtils
import com.azamovhudstc.tashkentmetro.R
import com.azamovhudstc.tashkentmetro.databinding.HomeScreenBinding
import com.azamovhudstc.tashkentmetro.utils.BaseFragment
import com.azamovhudstc.tashkentmetro.utils.LocalData
import com.azamovhudstc.tashkentmetro.utils.slideStart
import com.azamovhudstc.tashkentmetro.utils.slideUp
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
        binding.linearLayout.slideStart(900, 0)
        binding.train1.slideStart(900, 0)
        binding.train2.slideStart(900, 0)
        binding.relativeLayout2.slideUp(1000, 0)
        binding.trainsStatusRv.adapter = adapter
        adapter.submitList(LocalData.trainsStatusList)
    }

    @SuppressLint("ResourceAsColor")
    override fun onOffsetChanged(appBar: AppBarLayout?, i: Int) {

        if (mMaxScrollSize == 0) mMaxScrollSize = appBar!!.totalScrollRange
        val percentage = Math.abs(i) * 100 / mMaxScrollSize
        val cap = MathUtils.clamp((percent - percentage) / percent.toFloat(), 0f, 1f)
        val duration: Long = 900




        if (percentage >= percent && !isCollapsed) {
            isCollapsed = true
            val typedValue = TypedValue()
            val theme: Resources.Theme = requireContext().theme
            theme.resolveAttribute(
                R.color.white,
                typedValue,
                true
            )
            @ColorInt val selectedcolor: Int = R.color.white
            requireActivity().window.statusBarColor = selectedcolor
            ObjectAnimator.ofFloat(binding.appBarLayout, "translationX", 0f)
                .setDuration(duration).start()

        }
        if (percentage <= percent && isCollapsed) {
            isCollapsed = false
            requireActivity().window.statusBarColor =
                ContextCompat.getColor(requireActivity(), R.color.white)
            binding.textView.slideUp(900, 0)
            binding.relativeLayout.slideUp(900, 0)
            binding.linearLayout.slideStart(900, 0)
            binding.train1.slideStart(900, 0)
            binding.train2.slideStart(900, 0)
            binding.relativeLayout2.slideUp(1000, 0)


        }

    }

}