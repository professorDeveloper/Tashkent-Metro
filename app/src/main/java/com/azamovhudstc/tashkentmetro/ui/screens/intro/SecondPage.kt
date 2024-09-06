package com.azamovhudstc.tashkentmetro.ui.screens.intro

import androidx.navigation.fragment.findNavController
import com.azamovhudstc.infinityinsurance.utils.animationTransaction
import com.azamovhudstc.infinityinsurance.utils.setSafeOnClickListener
import com.azamovhudstc.tashkentmetro.R
import com.azamovhudstc.tashkentmetro.databinding.SecondPageBinding
import com.azamovhudstc.tashkentmetro.utils.BaseFragment


class SecondPage : BaseFragment<SecondPageBinding>(SecondPageBinding::inflate) {

    override fun onViewCreate() {
        binding.nextBtn.setSafeOnClickListener {
            findNavController().navigate(R.id.thirdPage, null, animationTransaction().build())
        }
    }
}