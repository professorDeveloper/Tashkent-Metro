package com.azamovhudstc.tashkentmetro.ui.screens.intro

import androidx.navigation.fragment.findNavController
import com.azamovhudstc.tashkentmetro.R
import com.azamovhudstc.tashkentmetro.databinding.FirstPageBinding
import com.azamovhudstc.tashkentmetro.utils.BaseFragment
import com.azamovhudstc.tashkentmetro.utils.alphaAnim
import com.azamovhudstc.tashkentmetro.utils.animationTransaction
import com.azamovhudstc.tashkentmetro.utils.setSafeOnClickListener
import com.azamovhudstc.tashkentmetro.utils.slideTop
import com.azamovhudstc.tashkentmetro.utils.slideUp
import com.azamovhudstc.tashkentmetro.utils.visible

class FirstPage : BaseFragment<FirstPageBinding>(FirstPageBinding::inflate) {

    override fun onViewCreate() {
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