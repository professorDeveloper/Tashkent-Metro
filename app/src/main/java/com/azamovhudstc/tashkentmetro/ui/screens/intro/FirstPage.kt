package com.azamovhudstc.tashkentmetro.ui.screens.intro

import androidx.navigation.fragment.findNavController
import com.azamovhudstc.infinityinsurance.utils.animationTransaction
import com.azamovhudstc.infinityinsurance.utils.setSafeOnClickListener
import com.azamovhudstc.tashkentmetro.R
import com.azamovhudstc.tashkentmetro.databinding.FirstPageBinding
import com.azamovhudstc.tashkentmetro.utils.BaseFragment

class FirstPage : BaseFragment<FirstPageBinding>(FirstPageBinding::inflate) {

    override fun onViewCreate() {
        val a = requireActivity()
//        val window = a.window
//        WindowCompat.setDecorFitsSystemWindows(window, true)

        binding.nextBtn.setSafeOnClickListener {

            findNavController().navigate(R.id.secondPage, null, animationTransaction().build())
        }

    }
}