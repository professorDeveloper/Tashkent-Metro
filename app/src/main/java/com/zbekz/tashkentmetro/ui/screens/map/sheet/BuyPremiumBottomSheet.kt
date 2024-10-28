package com.zbekz.tashkentmetro.ui.screens.map.sheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.zbekz.tashkentmetro.databinding.BuypremiumBottomSheetBinding

class BuyPremiumBottomSheet: BottomSheetDialogFragment() {
    private var _binding: BuypremiumBottomSheetBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BuypremiumBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

}