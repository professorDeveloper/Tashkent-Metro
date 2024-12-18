package com.zbekz.tashkentmetro.ui.screens.intro

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.zbekz.tashkentmetro.R
import com.zbekz.tashkentmetro.data.local.shp.AppReference
import com.zbekz.tashkentmetro.databinding.ThirdPageBinding
import com.zbekz.tashkentmetro.ui.activity.MainActivity
import com.zbekz.tashkentmetro.utils.BaseFragment
import com.zbekz.tashkentmetro.utils.setSafeOnClickListener
import com.zbekz.tashkentmetro.utils.slideStart
import com.zbekz.tashkentmetro.utils.slideUp
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ThirdPage : BaseFragment<ThirdPageBinding>(ThirdPageBinding::inflate) {

    @Inject
    lateinit var userPreferenceManager: AppReference

    private val locationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { result ->
            if (result) getLocation()
        }


    override fun onViewCreate(savedInstanceState: Bundle?) {
        binding.slides1.slideStart(800,0)
        binding.linearLayout3.slideStart(800,0)
        binding.textClock.slideUp(800,0)
        binding.nextContract.slideUp(800,0)
        binding.notNowTxt.slideUp(800,0)
        binding.circleImageView.slideUp(800,0)
        binding.nextContract.setSafeOnClickListener {
            // Check if the location permission is already granted
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                locationPermissionLauncher.launch(
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            } else {
                getLocation()
            }

        }

        binding.notNowTxt.setSafeOnClickListener {
            userPreferenceManager.setCurrentScreen("home")
            val intent = Intent(requireActivity(), MainActivity::class.java)
            startActivity(intent)
            requireActivity().finish()

        }
    }

    fun getLocation() {
       binding.contractNextBtnText.text=requireContext().getString(R.string.next)
        binding.nextContract.setSafeOnClickListener {
            userPreferenceManager.setCurrentScreen("home")
            val intent = Intent(requireActivity(), MainActivity::class.java)
            startActivity(intent)
            requireActivity().finish()

        }
    }
}