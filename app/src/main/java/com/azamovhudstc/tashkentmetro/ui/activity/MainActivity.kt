package com.azamovhudstc.tashkentmetro.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.azamovhudstc.tashkentmetro.R
import com.azamovhudstc.tashkentmetro.databinding.ActivityMainBinding
import com.azamovhudstc.tashkentmetro.utils.initActivity

class MainActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivityMainBinding

    private val navController: NavController by lazy {
        (supportFragmentManager.findFragmentById(R.id.navHost) as NavHostFragment).navController
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        viewBinding.homeNavigation.setupWithNavController(navController)

    }

}