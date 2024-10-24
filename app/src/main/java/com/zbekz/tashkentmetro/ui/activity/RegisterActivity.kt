package com.zbekz.tashkentmetro.ui.activity

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.graphics.Color
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.google.android.material.button.MaterialButton
import com.zbekz.tashkentmetro.R
import com.zbekz.tashkentmetro.data.local.shp.AppReference
import com.zbekz.tashkentmetro.databinding.RegisterScreenBinding
import com.zbekz.tashkentmetro.utils.ResendTimerUtil
import com.zbekz.tashkentmetro.utils.localizeDone
import com.zbekz.tashkentmetro.utils.localizeEnterCode
import com.zbekz.tashkentmetro.utils.setupPhoneNumberEditText
import com.zbekz.tashkentmetro.utils.visible
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RegisterActivity: AppCompatActivity() {


    private lateinit var timer: ResendTimerUtil
    private var phone: String = ""

    @Inject
    lateinit var userPreferenceManager: AppReference

    private lateinit var binding: RegisterScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = RegisterScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.statusBarColor = Color.parseColor("#F2F2F7")
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
            if (text == getString(R.string.next)) {
                showInputCode()
            } else {
                finishLogin()
            }
        }

        binding.buttonBackToSettings.setOnClickListener {
            onBackPressed()
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
        binding.nextBtn.text = localizeDone(this)
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
            receivedTxt.text = localizeEnterCode(this@RegisterActivity, maskPhone.text.toString())
        }
    }

    private fun finishLogin() {
        val pin = binding.inputCode.text.toString()
        if (pin == "444-444" && phone == "+998 00-000-00-00") {
            timer.stop()
            userPreferenceManager.userName = "Bekzod Rakhmatov"
            userPreferenceManager.userPhone = phone
            onBackPressed()
        }
    }

    override fun onResume() {
        super.onResume()
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
    }

    override fun onPause() {
        super.onPause()
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
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

    private fun changeButtonColorWithAnimation(button: Button, colorFrom: Int, colorTo: Int) {
        val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), colorFrom, colorTo)
        colorAnimation.duration = 300 // 300 ms davomiylik
        colorAnimation.addUpdateListener { animator ->
            button.setBackgroundColor(animator.animatedValue as Int)
        }
        colorAnimation.start()
    }

}