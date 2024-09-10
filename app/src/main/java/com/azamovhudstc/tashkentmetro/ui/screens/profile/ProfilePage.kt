package com.azamovhudstc.tashkentmetro.ui.screens.profile

import android.widget.PopupMenu
import androidx.fragment.app.viewModels
import com.azamovhudstc.tashkentmetro.R
import com.azamovhudstc.tashkentmetro.data.local.shp.AppReference
import com.azamovhudstc.tashkentmetro.data.local.shp.Language
import com.azamovhudstc.tashkentmetro.databinding.ProfilePageBinding
import com.azamovhudstc.tashkentmetro.utils.BaseFragment
import com.azamovhudstc.tashkentmetro.utils.slideStart
import com.azamovhudstc.tashkentmetro.utils.slideUp
import com.azamovhudstc.tashkentmetro.viewmodel.profile.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProfilePage: BaseFragment<ProfilePageBinding> (ProfilePageBinding::inflate){

    private val viewModel by viewModels<ProfileViewModel>()
    @Inject
    lateinit var userPreferenceManager: AppReference

    override fun onViewCreate() {
        // Access views via binding
        val dropdownIcon = binding.dropdownIcon

        binding.textView9.slideStart(800,0)
        binding.textView10.slideStart(800,0)
        binding.cardView2.slideUp(800,0)
        binding.cardView3.slideUp(800,0)
        binding.cardView4.slideUp(800,0)
        binding.cardview5.slideUp(800,0)
        binding.languageText.text = getLanguageString(userPreferenceManager.language)
        binding.flagImage.setImageResource(getFlagDrawable(userPreferenceManager.language))

        // Set up a PopupMenu to show a dropdown-like language selector
        dropdownIcon.setOnClickListener {
            val popupMenu = PopupMenu(requireActivity(), dropdownIcon)
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
    }


    private fun setAppLocale() {
        requireActivity().recreate()
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



}