package com.azamovhudstc.tashkentmetro.ui.screens.intro

import android.content.Intent
import android.util.Log
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.azamovhudstc.tashkentmetro.R
import com.azamovhudstc.tashkentmetro.databinding.SplashPageBinding
import com.azamovhudstc.tashkentmetro.ui.activity.MainActivity
import com.azamovhudstc.tashkentmetro.utils.BaseFragment
import com.azamovhudstc.tashkentmetro.utils.alphaAnim
import com.azamovhudstc.tashkentmetro.utils.animationTransactionClearStack
import com.azamovhudstc.tashkentmetro.utils.visible
import com.azamovhudstc.tashkentmetro.viewmodel.imp.IntroViewModelImpl
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplashPage : BaseFragment<SplashPageBinding>(SplashPageBinding::inflate) {
    private val model by viewModels<IntroViewModelImpl>()
    override fun onViewCreate() {

        lifecycleScope.launch {
            binding.appLogo.visible()
            binding.appLogo.alphaAnim()
            delay(1000)
            model.loginScreenLiveData.observe(viewLifecycleOwner, getStartedScreenObserver)
            model.homeScreenLiveData.observe(viewLifecycleOwner, openHomeScreenObserver)
            model.checkPage()

        }

    }

    private val openHomeScreenObserver = Observer<Unit> {
        val intent = Intent(requireActivity(), MainActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }
    private val getStartedScreenObserver = Observer<Unit> {

        findNavController().navigate(
            R.id.firstPage,
            null,
            animationTransactionClearStack(R.id.splashPage).build()
        )
    }

}