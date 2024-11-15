package com.zbekz.tashkentmetro.ui.screens.auth

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import com.zbekz.tashkentmetro.R
import com.zbekz.tashkentmetro.databinding.RegisterScreenBinding
import com.zbekz.tashkentmetro.data.local.shp.AppReference
import com.zbekz.tashkentmetro.ui.activity.MainActivity
import com.zbekz.tashkentmetro.utils.BaseFragment
import com.zbekz.tashkentmetro.utils.ResendTimerUtil
import com.zbekz.tashkentmetro.utils.localizeDone
import com.zbekz.tashkentmetro.utils.localizeEnterCode
import com.zbekz.tashkentmetro.utils.setupPhoneNumberEditText
import com.zbekz.tashkentmetro.utils.visible
import com.google.android.material.button.MaterialButton
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RegisterScreen : BaseFragment<RegisterScreenBinding>(RegisterScreenBinding::inflate) {
    private lateinit var timer: ResendTimerUtil
    private var phone: String = ""

    @Inject
    lateinit var userPreferenceManager: AppReference

    override fun onViewCreate(savedInstanceState: Bundle?) {
        binding.maskPhone.setupPhoneNumberEditText(
            onChangedToEnable = { enableButton(binding.nextBtn) },
            onChangedToDisable = { disableButton(binding.nextBtn) })

        binding.apply {
            inputCode.addTextChangedListener {
                if (it.toString().length == 7) {
                    enableButton(nextBtn)
                } else {
                    disableButton(nextBtn)
                }
            }
        }

        binding.nextBtn.setOnClickListener {
            val text = binding.nextBtn.text
            if (text == "Next") {
                showInputCode()
            } else {
                finishLogin()
            }
        }

        binding.buttonBackToSettings.setOnClickListener {
            navController.navigateUp()
        }

        binding.buttonResendCode.setOnClickListener {
            timer.start()
            binding.buttonResendCode.isEnabled = false
        }

        timer = ResendTimerUtil(
            oonTick = { binding.resendTimerTv.text = it },
            oonFinish = { binding.buttonResendCode.isEnabled = true }
        )
    }

    private fun showInputCode() {
        visibleAllItems()
        binding.nextBtn.text = localizeDone(requireContext())
        phone = binding.maskPhone.text.toString()
        disableButton(binding.nextBtn)
        binding.inputCode.requestFocus()
        binding.maskPhone.isEnabled = false
        timer.start()
    }

    private fun visibleAllItems() {
        with(binding) {
            dividerEdit.visible()
            buttonEditPhoneNumber.visible()
            groupEnterCode.visible()
            receivedTxt.text = localizeEnterCode(requireContext(), maskPhone.text.toString())
        }
    }

    private fun finishLogin() {
        val pin = binding.inputCode.text.toString()
        if (pin == "444-444" && phone == "+998 00-000-00-00") {
            timer.stop()
            userPreferenceManager.userName = "Bekzod Rakhmatov"
            userPreferenceManager.userPhone = phone
            navController.navigateUp()
        }
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

    private fun disableButton(button: MaterialButton) {
        button.isEnabled = false
    }

    private fun enableButton(button: MaterialButton) {
        button.isEnabled = true
        button.animate()
            .alpha(1f)
            .scaleX(1f)
            .scaleY(1f)
            .setDuration(300)
            .start()
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