package com.azamovhudstc.tashkentmetro.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.navigation.ui.setupWithNavController
import com.azamovhudstc.tashkentmetro.R
import com.azamovhudstc.tashkentmetro.databinding.ActivityIntroBinding
import com.azamovhudstc.tashkentmetro.databinding.ActivityMainBinding
import com.azamovhudstc.tashkentmetro.utils.initActivity

class IntroActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivityIntroBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        initActivity(this)



    }


}