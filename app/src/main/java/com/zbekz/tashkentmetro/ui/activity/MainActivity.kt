package com.zbekz.tashkentmetro.ui.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.zbekz.tashkentmetro.R
import com.zbekz.tashkentmetro.data.local.shp.AppReference
import com.zbekz.tashkentmetro.databinding.ActivityMainBinding
import com.zbekz.tashkentmetro.ui.screens.map.MapScreen
import com.zbekz.tashkentmetro.ui.screens.profile.ProfilePage
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

    private lateinit var mapFragment: MapScreen
    private lateinit var profileFragment: ProfilePage
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        ViewUtils.setLanguageForService(this, userPreferenceManager)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        setupFragments()

        viewBinding.homeNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.button_maps -> {
                    showFragment(mapFragment)
                    true
                }

                R.id.button_settings -> {
                    showFragment(profileFragment)
                    true
                }

                else -> false
            }
        }

        if (savedInstanceState == null) {
            showFragment(mapFragment)
        }
//        viewBinding.homeNavigation.setupWithNavController(navController)

    }

    private fun setupFragments() {

        mapFragment = MapScreen()
        profileFragment = ProfilePage()

        supportFragmentManager.beginTransaction().apply {
            add(R.id.navHost, mapFragment, "MAP_FRAGMENT")
            add(R.id.navHost, profileFragment, "PROFILE_FRAGMENT")
            hide(mapFragment)
            hide(profileFragment)
        }.commit()
    }

    private fun showFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            supportFragmentManager.fragments.forEach { hide(it) }
            show(fragment)
        }.commit()
    }


    fun hideBottomNavigation() {
        viewBinding.homeNavigation.visibility = View.GONE
    }

    fun showBottomNavigation() {
        viewBinding.homeNavigation.visibility = View.VISIBLE
    }

}