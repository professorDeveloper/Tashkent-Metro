package com.zbekz.tashkentmetro.ui.screens.map.sheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.zbekz.tashkentmetro.databinding.BuypremiumBottomSheetBinding
import com.zbekz.tashkentmetro.viewmodel.subscription.SubscriptionViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BuyPremiumBottomSheet : BottomSheetDialogFragment() {

    private var _binding: BuypremiumBottomSheetBinding? = null
    private val viewModel by viewModels<SubscriptionViewModel>()
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

        binding.monthlyCardBtn.setOnClickListener {
            viewModel.selectSubscription(SubscriptionViewModel.SubscriptionType.MONTHLY)
        }
        binding.yearlyCardBtn.setOnClickListener {
            viewModel.selectSubscription(SubscriptionViewModel.SubscriptionType.YEARLY)
        }
        binding.closeButton.setOnClickListener { dismiss() }

        lifecycleScope.launch {
            viewModel.selectedSubscription.collect { selectedType ->
                when (selectedType) {
                    SubscriptionViewModel.SubscriptionType.MONTHLY -> {
                        binding.subscribeButton.isEnabled=true
                        binding.monthlyRadioButton.isChecked = true
                        binding.yearlyRadioButton.isChecked = false
                    }
                    SubscriptionViewModel.SubscriptionType.YEARLY -> {
                        binding.subscribeButton.isEnabled=true
                        binding.monthlyRadioButton.isChecked = false
                        binding.yearlyRadioButton.isChecked = true
                    }
                    else -> {
                        binding.subscribeButton.isEnabled=false
                        binding.monthlyRadioButton.isChecked = false
                        binding.yearlyRadioButton.isChecked = false
                    }
                }
            }
        }

        binding.subscribeButton.setOnClickListener {
            viewModel.initiatePurchase()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
