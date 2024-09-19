package com.azamovhudstc.tashkentmetro.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.azamovhudstc.tashkentmetro.data.local.shp.AppReference
import com.azamovhudstc.tashkentmetro.databinding.ActivityIntroBinding
import com.azamovhudstc.tashkentmetro.utils.ViewUtils
import com.azamovhudstc.tashkentmetro.utils.initActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class IntroActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivityIntroBinding
    @Inject
    lateinit var userPreferenceManager: AppReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        initActivity(this)



    }



}