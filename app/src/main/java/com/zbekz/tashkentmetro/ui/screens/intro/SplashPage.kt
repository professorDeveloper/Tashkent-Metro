package com.zbekz.tashkentmetro.ui.screens.intro

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.zbekz.tashkentmetro.R
import com.zbekz.tashkentmetro.databinding.SplashPageBinding
import com.zbekz.tashkentmetro.ui.activity.MainActivity
import com.zbekz.tashkentmetro.utils.BaseFragment
import com.zbekz.tashkentmetro.utils.alphaAnim
import com.zbekz.tashkentmetro.utils.animationTransactionClearStack
import com.zbekz.tashkentmetro.utils.visible
import com.zbekz.tashkentmetro.viewmodel.imp.IntroViewModelImpl
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplashPage : BaseFragment<SplashPageBinding>(SplashPageBinding::inflate) {
    private val model by viewModels<IntroViewModelImpl>()
    override fun onViewCreate(savedInstanceState: Bundle?) {

//        lifecycleScope.launch {
//            binding.appLogo.visible()
//            binding.appLogo.alphaAnim()
//            delay(1000)
//            model.loginScreenLiveData.observe(viewLifecycleOwner, getStartedScreenObserver)
//            model.homeScreenLiveData.observe(viewLifecycleOwner, openHomeScreenObserver)
//            model.checkPage()
//        }

    }

    private val openHomeScreenObserver = Observer<Unit> {
        val intent = Intent(requireActivity(), MainActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }
//    private val getStartedScreenObserver = Observer<Unit> {
//
//        findNavController().navigate(
//            R.id.firstPage,
//            null,
//            animationTransactionClearStack(R.id.splashPage).build()
//        )
//    }

}