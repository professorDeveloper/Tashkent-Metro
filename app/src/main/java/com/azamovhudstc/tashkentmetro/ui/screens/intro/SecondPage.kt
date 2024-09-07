package com.azamovhudstc.tashkentmetro.ui.screens.intro

import androidx.navigation.fragment.findNavController
import com.azamovhudstc.tashkentmetro.R
import com.azamovhudstc.tashkentmetro.databinding.SecondPageBinding
import com.azamovhudstc.tashkentmetro.utils.BaseFragment
import com.azamovhudstc.tashkentmetro.utils.animationTransaction
import com.azamovhudstc.tashkentmetro.utils.setSafeOnClickListener
import com.azamovhudstc.tashkentmetro.utils.slideTop
import com.azamovhudstc.tashkentmetro.utils.slideUp
import com.azamovhudstc.tashkentmetro.utils.visible


class SecondPage : BaseFragment<SecondPageBinding>(SecondPageBinding::inflate) {

    override fun onViewCreate() {
        binding.apply {
            nextBtn.slideUp(1000, 0)
            gradientMaterialButton.slideTop(1000, 0)
            infoBtn.setSafeOnClickListener {

            }
        }
        binding.nextBtn.setSafeOnClickListener {
            binding.apply {
            }
            findNavController().navigate(R.id.thirdPage, null, animationTransaction().build())
        }
    }
}