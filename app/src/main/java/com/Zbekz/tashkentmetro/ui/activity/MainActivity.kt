package com.Zbekz.tashkentmetro.ui.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.Zbekz.tashkentmetro.R
import com.Zbekz.tashkentmetro.data.local.shp.AppReference
import com.Zbekz.tashkentmetro.databinding.ActivityMainBinding
import com.Zbekz.tashkentmetro.utils.ViewUtils
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

    }


    fun hideBottomNavigation() {
        viewBinding.homeNavigation.visibility = View.GONE
    }

    fun showBottomNavigation() {
        viewBinding.homeNavigation.visibility = View.VISIBLE
    }

}