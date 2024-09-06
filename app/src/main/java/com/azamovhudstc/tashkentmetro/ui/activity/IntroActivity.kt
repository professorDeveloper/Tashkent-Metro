package com.azamovhudstc.tashkentmetro.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.azamovhudstc.tashkentmetro.databinding.ActivityIntroBinding
import com.azamovhudstc.tashkentmetro.domain.preference.UserPreferenceManager
import com.azamovhudstc.tashkentmetro.utils.initActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class IntroActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivityIntroBinding

    @Inject
    lateinit var userPreferenceManager: UserPreferenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityIntroBinding.inflate(layoutInflater)
        if (userPreferenceManager.getSeenIntro()) {
            openHome()
        } else {
            setContentView(viewBinding.root)
        }
        initActivity(this)


    }

    private fun openHome() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }


}