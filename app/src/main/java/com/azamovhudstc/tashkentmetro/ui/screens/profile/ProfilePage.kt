package com.azamovhudstc.tashkentmetro.ui.screens.profile

import android.content.Intent
import android.net.Uri
import android.widget.PopupMenu
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.azamovhudstc.tashkentmetro.R
import com.azamovhudstc.tashkentmetro.data.local.shp.AppReference
import com.azamovhudstc.tashkentmetro.data.local.shp.Language
import com.azamovhudstc.tashkentmetro.data.local.shp.ThemeStyle
import com.azamovhudstc.tashkentmetro.databinding.ProfilePageBinding
import com.azamovhudstc.tashkentmetro.utils.BaseFragment
import com.azamovhudstc.tashkentmetro.utils.animationTransaction
import com.azamovhudstc.tashkentmetro.utils.snackString
import com.azamovhudstc.tashkentmetro.viewmodel.profile.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProfilePage : BaseFragment<ProfilePageBinding>(ProfilePageBinding::inflate) {

    private val viewModel by viewModels<ProfileViewModel>()

    @Inject
    lateinit var userPreferenceManager: AppReference

    override fun onViewCreate() {
        // Access views via binding
        val dropdownIcon = binding.dropdownIcon
        val dropdownIconTheme = binding.dropdownIconTheme

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
        binding.contactUs.setOnClickListener {
            val tgContact = Uri.parse("https://bekzodrakhmatof.t.me")
            val intent = Intent(Intent.ACTION_VIEW, tgContact)

            if (intent.resolveActivity(requireActivity().packageManager) != null) {
                startActivity(intent)
            } else {
                snackString("No app available to open this link")
            }

        }

        dropdownIconTheme.setOnClickListener {
            showPopupMenuTheme()
        }

        binding.loginRegisterTxt.setOnClickListener {
            findNavController().navigate(
                R.id.registerPage, null,
                animationTransaction().build()
            )
        }
    }


    private fun setAppLocale() {
        requireActivity().recreate()
    }

    private fun showPopupMenuLanguage() {
        val popupMenu = PopupMenu(requireActivity(), binding.dropdownIcon)
        popupMenu.menuInflater.inflate(R.menu.language_menu, popupMenu.menu)


        // Handle language selection from the popup menu
        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.language_english -> {
                    viewModel.setLanguage(Language.ENGLISH)
                    setAppLocale()
                }

                R.id.language_russian -> {
                    viewModel.setLanguage(Language.RUSSIAN)
                    setAppLocale()
                }

                R.id.language_uzbek -> {
                    viewModel.setLanguage(Language.UZBEK)
                    setAppLocale()
                }
            }
            true
        }

        // Show the popup menu
        popupMenu.show()
    }

    private fun showPopupMenuTheme() {
        val popupMenu = PopupMenu(requireActivity(), binding.dropdownIconTheme)
        popupMenu.menuInflater.inflate(R.menu.theme_menu, popupMenu.menu)


        // Handle language selection from the popup menu
        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.theme_auto -> {
                    viewModel.setTheme(ThemeStyle.AUTO)
                }

                R.id.theme_day -> {
                    viewModel.setTheme(ThemeStyle.LIGHT)

                }

                R.id.theme_night -> {
                    viewModel.setTheme(ThemeStyle.DARK)

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
            ThemeStyle.DARK -> getString(R.string.night)
            ThemeStyle.LIGHT -> getString(R.string.day)
        }
    }


}