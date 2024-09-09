package com.azamovhudstc.tashkentmetro.ui.screens.profile

import android.widget.PopupMenu
import com.azamovhudstc.tashkentmetro.R
import com.azamovhudstc.tashkentmetro.databinding.ProfilePageBinding
import com.azamovhudstc.tashkentmetro.utils.BaseFragment

class ProfilePage: BaseFragment<ProfilePageBinding> (ProfilePageBinding::inflate){
    override fun onViewCreate() {
        // Access views via binding
        val dropdownIcon = binding.dropdownIcon
        val flagImage = binding.flagImage
        val languageText = binding.languageText

        // Set up a PopupMenu to show a dropdown-like language selector
        dropdownIcon.setOnClickListener {
            val popupMenu = PopupMenu(requireActivity(), dropdownIcon)
            popupMenu.menuInflater.inflate(R.menu.language_menu, popupMenu.menu)

            // Handle language selection from the popup menu
            popupMenu.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.language_english -> {
                        languageText.text = "English"
                        flagImage.setImageResource(R.drawable.uk) // Update flag
                    }
                    R.id.language_spanish -> {
                        languageText.text = "Spanish"
                        flagImage.setImageResource(R.drawable.uk) // Update flag
                    }
                    R.id.language_french -> {
                        languageText.text = "French"
                        flagImage.setImageResource(R.drawable.uk) // Update flag
                    }
                }
                true
            }

            // Show the popup menu
            popupMenu.show()
        }
    }
}