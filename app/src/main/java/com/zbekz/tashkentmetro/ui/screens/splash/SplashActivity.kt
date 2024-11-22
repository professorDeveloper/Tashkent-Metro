package com.zbekz.tashkentmetro.ui.screens.splash

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.zbekz.tashkentmetro.R
import com.zbekz.tashkentmetro.data.local.shp.AppReference
import com.zbekz.tashkentmetro.databinding.SplashPageBinding
import com.zbekz.tashkentmetro.ui.activity.IntroActivity
import com.zbekz.tashkentmetro.ui.activity.MainActivity
import com.zbekz.tashkentmetro.utils.enums.CurrentScreenEnum
import com.zbekz.tashkentmetro.utils.initActivity
import com.zbekz.tashkentmetro.utils.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    @Inject
    lateinit var userPreferenceManager: AppReference

    private lateinit var binding: SplashPageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SplashPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initActivity(this)

        lifecycleScope.launch {
            binding.appLogo.visible()
            binding.appLogo.alphaAnim(this@SplashActivity)
            delay(1000)

            if (userPreferenceManager.currentScreenEnum == CurrentScreenEnum.HOME) {
                openHome()
            } else {
                openIntro()
            }
        }


    }

    private fun View.alphaAnim(context: Context) {
        val anim = AnimationUtils.loadAnimation(
            context,
            R.anim.alpha_anim
        ).apply {
            duration = 1800L

            fillAfter = true
        }

        startAnimation(anim)

    }

    private fun openIntro() {
        val intent = Intent(this@SplashActivity, IntroActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun openHome() {
        val intent = Intent(this@SplashActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

}