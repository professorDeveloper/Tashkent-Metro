package com.zbekz.tashkentmetro.ui.screens.map.sheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.zbekz.tashkentmetro.databinding.BuypremiumBottomSheetBinding

class BuyPremiumBottomSheet : BottomSheetDialogFragment() {

    private var _binding: BuypremiumBottomSheetBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BuypremiumBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Close button action
        binding.closeButton.setOnClickListener {
            dismiss()
        }

        // Load offerings

        // Handle subscription selection
        binding.monthlyCardBtn.setOnClickListener {
            binding.monthlyCardBtn.isChecked = true
            binding.yearlyCardBtn.isChecked = false
//            selectedPackage = null // Clear selection
        }

        binding.yearlyCardBtn.setOnClickListener {
            binding.yearlyCardBtn.isChecked = true
            binding.monthlyCardBtn.isChecked = false
//            selectedPackage = null // Clear selection
        }

        binding.subscribeButton.setOnClickListener {
//            if (selectedPackage != null) {
//            purchaseSubscription()
//            } else {
//                Toast.makeText(requireContext(), "Please select a subscription plan", Toast.LENGTH_SHORT).show()
//            }
            dismiss()
        }
    }


    private fun purchaseSubscription() {

    }

//        selectedPackage?.let { packageToPurchase ->
//            Purchases.sharedInstance.purchasePackage(requireActivity(), packageToPurchase,({error-> })) { purchase ->
//
//                if (purchase?.isPurchased == true) {
//                    Toast.makeText(requireContext(), "Purchase successful!", Toast.LENGTH_SHORT).show()
//                    // Handle successful purchase (e.g., unlock features)
//                }
//            }
//        }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
