package com.azamovhudstc.tashkentmetro.ui.screens.auth

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import androidx.core.content.ContextCompat
import com.azamovhudstc.tashkentmetro.R
import com.azamovhudstc.tashkentmetro.databinding.RegisterScreenBinding
import com.azamovhudstc.tashkentmetro.ui.activity.MainActivity
import com.azamovhudstc.tashkentmetro.utils.BaseFragment
import com.azamovhudstc.tashkentmetro.utils.setupPhoneNumberEditText
import com.google.android.material.button.MaterialButton

class RegisterScreen : BaseFragment<RegisterScreenBinding>(RegisterScreenBinding::inflate) {
    override fun onViewCreate() {

        binding.maskPhone.setupPhoneNumberEditText(
            onChangedToEnable = {enableButton(binding.nextBtn)},
            onChangedToDisable = {disableButton(binding.nextBtn)})
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as? MainActivity)?.hideBottomNavigation()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as? MainActivity)?.showBottomNavigation()
    }

    override fun onResume() {
        super.onResume()
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
    }

    override fun onPause() {
        super.onPause()
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
    }


    private fun enableButtonWithAnimation(button: MaterialButton) {
        button.isEnabled = true

        changeButtonColorWithAnimation(
            button,
            ContextCompat.getColor(requireActivity(), R.color.bg_black_50),
            ContextCompat.getColor(requireActivity(), R.color.colorPrimary)
        )

        button.animate()
            .alpha(1f)
            .scaleX(1f)
            .scaleY(1f)
            .setDuration(300)
            .start()
    }

    private fun disableButton(button: MaterialButton){
        button.isEnabled = false
    }
    private fun enableButton(button: MaterialButton){
        button.isEnabled = true
    }

    fun disableButtonWithAnimation(button: MaterialButton) {
        button.isEnabled = false


        changeButtonColorWithAnimation(
            button,
            ContextCompat.getColor(requireActivity(), R.color.colorPrimary),
            ContextCompat.getColor(requireActivity(), R.color.bg_black_50)
        )
        button.animate()
            .alpha(0.5f)
            .scaleX(0.8f)
            .scaleY(0.8f)
            .setDuration(300)
            .start()
    }

    private fun changeButtonColorWithAnimation(button: Button, colorFrom: Int, colorTo: Int) {
        val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), colorFrom, colorTo)
        colorAnimation.duration = 300 // 300 ms davomiylik
        colorAnimation.addUpdateListener { animator ->
            button.setBackgroundColor(animator.animatedValue as Int)
        }
        colorAnimation.start()
    }
}