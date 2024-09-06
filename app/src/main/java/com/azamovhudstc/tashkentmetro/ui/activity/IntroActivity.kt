package com.azamovhudstc.tashkentmetro.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.azamovhudstc.tashkentmetro.databinding.ActivityIntroBinding
import com.azamovhudstc.tashkentmetro.utils.initActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IntroActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivityIntroBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        initActivity(this)


    }



}