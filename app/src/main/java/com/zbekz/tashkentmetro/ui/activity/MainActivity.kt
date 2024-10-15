package com.zbekz.tashkentmetro.ui.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.zbekz.tashkentmetro.R
import com.zbekz.tashkentmetro.data.local.shp.AppReference
import com.zbekz.tashkentmetro.databinding.ActivityMainBinding
import com.zbekz.tashkentmetro.utils.ViewUtils
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivityMainBinding

    @Inject
    lateinit var userPreferenceManager: AppReference

    private val navController: NavController by lazy {
        (supportFragmentManager.findFragmentById(R.id.navHost) as NavHostFragment).navController
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        ViewUtils.setLanguageForService(this, userPreferenceManager)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)


        viewBinding.homeNavigation.setupWithNavController(navController)

        viewBinding.homeNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.button_maps -> {
                    navController.navigate(R.id.button_maps)
                    true
                }
                R.id.button_settings -> {
                    navController.navigate(R.id.button_settings, null, NavOptions.Builder()
                        .setPopUpTo(navController.graph.startDestinationId, true)
                        .build())
                    true
                }
                else -> false
            }
        }
    }


    fun hideBottomNavigation() {
        viewBinding.homeNavigation.visibility = View.GONE
    }

    fun showBottomNavigation() {
        viewBinding.homeNavigation.visibility = View.VISIBLE
    }

    override fun onBackPressed() {
        if (navController.currentDestination?.id == R.id.button_settings) {
            finish()
        } else {
            super.onBackPressed()
        }
    }
}