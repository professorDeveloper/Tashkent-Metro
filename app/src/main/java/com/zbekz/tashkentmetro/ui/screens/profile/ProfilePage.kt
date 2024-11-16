package com.zbekz.tashkentmetro.ui.screens.profile

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.PopupMenu
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.zbekz.tashkentmetro.R
import com.zbekz.tashkentmetro.data.local.shp.AppReference
import com.zbekz.tashkentmetro.data.local.shp.Language
import com.zbekz.tashkentmetro.data.local.shp.ThemeStyle
import com.zbekz.tashkentmetro.databinding.ProfilePageBinding
import com.zbekz.tashkentmetro.ui.activity.RegisterActivity
import com.zbekz.tashkentmetro.utils.BaseFragment
import com.zbekz.tashkentmetro.utils.gone
import com.zbekz.tashkentmetro.utils.visible
import com.zbekz.tashkentmetro.viewmodel.profile.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import javax.inject.Inject

@AndroidEntryPoint
class ProfilePage : BaseFragment<ProfilePageBinding>(ProfilePageBinding::inflate) {

    private val viewModel by viewModels<ProfileViewModel>()

    @Inject
    lateinit var userPreferenceManager: AppReference

    @SuppressLint("SuspiciousIndentation")
    override fun onViewCreate(savedInstanceState: Bundle?) {
        // Access views via binding
        val dropdownIcon = binding.dropdownIcon
        val dropdownIconTheme = binding.dropdownIconTheme

        binding.contactUs.setOnClickListener {
            val tgContact = Uri.parse("https://t.me/bekzodrakhmatof")
            val intent = Intent(Intent.ACTION_VIEW, tgContact)
                startActivity(intent)
        }
        binding.loginTv.text =
            if (userPreferenceManager.userName == "null") getString(R.string.login_register) else userPreferenceManager.userName
        binding.phoneNumberTv.text =
            if (userPreferenceManager.userName == "null") getString(R.string.contribute_join_leaderboard) else userPreferenceManager.userPhone

        binding.languageText.text = getLanguageString(userPreferenceManager.language)
        binding.themeTxt.text = getThemeText()
        binding.flagImage.setImageResource(getFlagDrawable(userPreferenceManager.language))

        binding.frameLanguage.setOnClickListener { showPopupMenuLanguage() }

        // Set up a PopupMenu to show a dropdown-like language selector
        dropdownIcon.setOnClickListener {
            showPopupMenuLanguage()
        }


        dropdownIconTheme.setOnClickListener {
            showPopupMenuTheme()
        }

        binding.loginRegisterTxt.setOnClickListener {
//            findNavController().navigate(
//                R.id.registerPage, null,
//                animationTransaction().build()
//            )
           navigateRegisterAndLogin()
        }

        binding.privacyPolicy.setOnClickListener {
            val tgContact = Uri.parse("https://zbekz.com/products/tashkent-metro/privacy-policy")
            val intent = Intent(Intent.ACTION_VIEW, tgContact)

            startActivity(intent)

        }
    }

    private fun navigateRegisterAndLogin(){
        val intent = Intent(requireContext(), RegisterActivity::class.java)
        startActivity(intent)
    }

    private fun showPopupMenuLanguage() {
        val popupMenu = PopupMenu(requireActivity(), binding.dropdownIcon)
        popupMenu.menuInflater.inflate(R.menu.language_menu, popupMenu.menu)


        // Handle language selection from the popup menu
        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.language_english -> viewModel.setLanguage(Language.ENGLISH)
                R.id.language_russian -> viewModel.setLanguage(Language.RUSSIAN)
                R.id.language_uzbek -> viewModel.setLanguage(Language.UZBEK)
            }
            updateAppLocale()
            true
        }

        // Show the popup menu
        popupMenu.show()
    }

    private fun updateAppLocale() {
        requireActivity().recreate()

    }

    private fun showPopupMenuTheme() {
        val popupMenu = PopupMenu(requireActivity(), binding.dropdownIconTheme)
        popupMenu.menuInflater.inflate(R.menu.theme_menu, popupMenu.menu)

        // Handle language selection from the popup menu
        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.theme_auto -> {

                    lifecycleScope.launchWhenResumed {
                        popupMenu.dismiss()
                        binding.progress.visible()
                        binding.mainContainer.gone()
                        delay(280)
                        viewModel.setTheme(ThemeStyle.AUTO)
                        binding.progress.gone()
                        binding.mainContainer.visible()
                    }
                }

                R.id.theme_day -> {
                    lifecycleScope.launchWhenResumed {
                        popupMenu.dismiss()
                        binding.progress.visible()
                        binding.mainContainer.gone()
                        delay(280)
                        viewModel.setTheme(ThemeStyle.LIGHT)
                        binding.progress.gone()
                        binding.mainContainer.visible()
                    }
                }

                R.id.theme_night -> {
                    lifecycleScope.launchWhenResumed {
                        popupMenu.dismiss()
                        binding.progress.visible()
                        binding.mainContainer.gone()
                        delay(280)
                        viewModel.setTheme(ThemeStyle.DARK)
                        binding.progress.gone()
                        binding.mainContainer.visible()
                    }
                }
            }
            true
        }

        // Show the popup menu
        popupMenu.show()
    }


    private fun getLanguageString(language: Language): String {
        return when (language) {
            Language.ENGLISH -> getString(R.string.english)
            Language.RUSSIAN -> getString(R.string.russian)
            Language.UZBEK -> getString(R.string.uzbek)
        }
    }

    private fun getFlagDrawable(language: Language): Int {
        return when (language) {
            Language.ENGLISH -> R.drawable.uk
            Language.RUSSIAN -> R.drawable.flag_russian
            Language.UZBEK -> R.drawable.flag_uzbekistan
        }
    }

    private fun getThemeText(): String {
        return when (userPreferenceManager.theme) {
            ThemeStyle.AUTO -> getString(R.string.auto)
            ThemeStyle.DARK -> getString(R.string.dark)
            ThemeStyle.LIGHT -> getString(R.string.light)
        }
    }


}