package com.Zbekz.tashkentmetro.ui.screens.intro

import android.content.Intent
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.Zbekz.tashkentmetro.R
import com.Zbekz.tashkentmetro.databinding.SplashPageBinding
import com.Zbekz.tashkentmetro.ui.activity.MainActivity
import com.Zbekz.tashkentmetro.utils.BaseFragment
import com.Zbekz.tashkentmetro.utils.alphaAnim
import com.Zbekz.tashkentmetro.utils.animationTransactionClearStack
import com.Zbekz.tashkentmetro.utils.visible
import com.Zbekz.tashkentmetro.viewmodel.imp.IntroViewModelImpl
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