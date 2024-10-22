package com.Zbekz.tashkentmetro.ui.screens.intro

import android.os.Bundle
import androidx.navigation.fragment.findNavController
import com.Zbekz.tashkentmetro.R
import com.Zbekz.tashkentmetro.databinding.SecondPageBinding
import com.Zbekz.tashkentmetro.utils.BaseFragment
import com.Zbekz.tashkentmetro.utils.animationTransaction
import com.Zbekz.tashkentmetro.utils.setSafeOnClickListener
import com.Zbekz.tashkentmetro.utils.slideTop
import com.Zbekz.tashkentmetro.utils.slideUp


class SecondPage : BaseFragment<SecondPageBinding>(SecondPageBinding::inflate) {

    override fun onViewCreate(savedInstanceState: Bundle?) {

        binding.textView5.slideUp(800,0)
        binding.textView6.slideUp(800,0)
        binding.cardView.slideUp(800,0)
        binding.textView7.slideUp(800,0)
        binding.textView9.slideUp(800,0)
        binding.nextBtn.slideUp(800,0)
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