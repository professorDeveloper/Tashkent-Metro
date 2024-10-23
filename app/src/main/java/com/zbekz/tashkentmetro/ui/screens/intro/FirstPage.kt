package com.zbekz.tashkentmetro.ui.screens.intro

import android.os.Bundle
import androidx.navigation.fragment.findNavController
import com.zbekz.tashkentmetro.R
import com.zbekz.tashkentmetro.databinding.FirstPageBinding
import com.zbekz.tashkentmetro.utils.BaseFragment
import com.zbekz.tashkentmetro.utils.alphaAnim
import com.zbekz.tashkentmetro.utils.animationTransaction
import com.zbekz.tashkentmetro.utils.setSafeOnClickListener
import com.zbekz.tashkentmetro.utils.slideTop
import com.zbekz.tashkentmetro.utils.slideUp
import com.zbekz.tashkentmetro.utils.visible

class FirstPage : BaseFragment<FirstPageBinding>(FirstPageBinding::inflate) {

    override fun onViewCreate(savedInstanceState: Bundle?) {
        val a = requireActivity()
//        val window = a.window
//        WindowCompat.setDecorFitsSystemWindows(window, true)

        binding.nextBtn.slideUp(900, 0)
        binding.textView4.visible()
        binding.textView2.visible()
        binding.textView3.visible()
        binding.timeTxt.visible()
        binding.textView4.alphaAnim()
        binding.textView3.alphaAnim()
        binding.textView2.alphaAnim()
        binding.timeTxt.alphaAnim()
        binding.logoContainer.slideTop(900, 0)
        binding.nextBtn.setSafeOnClickListener {

            findNavController().navigate(R.id.secondPage, null, animationTransaction().build())
        }

    }
}