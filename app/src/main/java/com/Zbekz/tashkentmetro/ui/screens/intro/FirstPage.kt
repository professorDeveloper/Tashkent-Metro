package com.Zbekz.tashkentmetro.ui.screens.intro

import android.os.Bundle
import androidx.navigation.fragment.findNavController
import com.Zbekz.tashkentmetro.R
import com.Zbekz.tashkentmetro.databinding.FirstPageBinding
import com.Zbekz.tashkentmetro.utils.BaseFragment
import com.Zbekz.tashkentmetro.utils.alphaAnim
import com.Zbekz.tashkentmetro.utils.animationTransaction
import com.Zbekz.tashkentmetro.utils.setSafeOnClickListener
import com.Zbekz.tashkentmetro.utils.slideTop
import com.Zbekz.tashkentmetro.utils.slideUp
import com.Zbekz.tashkentmetro.utils.visible

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