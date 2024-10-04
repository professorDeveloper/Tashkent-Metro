package com.azamovhudstc.tashkentmetro.utils.view

import android.animation.Animator
import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewAnimationUtils
import android.widget.EditText
import android.widget.FrameLayout
import androidx.core.widget.addTextChangedListener
import com.azamovhudstc.tashkentmetro.data.model.station.Station
import com.azamovhudstc.tashkentmetro.databinding.ViewSearchBinding
import com.azamovhudstc.tashkentmetro.ui.screens.map.SearchViewAdapter
import com.azamovhudstc.tashkentmetro.utils.LocalData
import com.azamovhudstc.tashkentmetro.utils.invisible
import com.azamovhudstc.tashkentmetro.utils.visible

class SearchView(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs) {

    private val adapter by lazy { SearchViewAdapter() }
    private var itemList: ArrayList<Station> =
        LocalData.popularStations.toMutableList() as ArrayList<Station>// Asl ro'yxat
    private var filteredList: ArrayList<Station> = ArrayList()

    private val binding: ViewSearchBinding =
        ViewSearchBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        listenKeyboardEvents(binding.searchInputText)
        binding.openSearchButton.setOnClickListener { openSearch() }
        binding.closeSearchButton.setOnClickListener { closeSearch() }
        binding.searchInputText.addTextChangedListener {
            if (it.toString().isEmpty()) {
                binding.rvFrame.invisible()
            } else {
                binding.rvFrame.visible()
                filterList(it.toString())
            }
        }
    }

    private fun listenKeyboardEvents(editText: EditText) {
        editText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && editText.text.toString().isNotEmpty()) {
                // Keyboard is open (or will open), make the target view visible
                binding.rvFrame.visibility = View.VISIBLE
            } else {
                // Keyboard is dismissed (or will be dismissed), make the target view invisible
                binding.rvFrame.visibility = View.INVISIBLE
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun filterList(text: String) {
        filteredList.clear()
        for (item in itemList) {
            if (item.name.contains(text, ignoreCase = true)) {
                filteredList.add(item)
            }
        }
        if (!filteredList.isEmpty()) {
            binding.rvFrame.visible()
            binding.searchViewRv.adapter = adapter
            adapter.submitList(filteredList)
            adapter.notifyDataSetChanged()

        } else {
            binding.rvFrame.invisible()
        }
    }


    private fun openSearch() {
        binding.searchInputText.setText("")
        binding.searchOpenView.visibility = View.VISIBLE
        val circularReveal = ViewAnimationUtils.createCircularReveal(
            binding.searchOpenView,
            (binding.openSearchButton.right + binding.openSearchButton.left) / 2,
            (binding.openSearchButton.top + binding.openSearchButton.bottom) / 2,
            0f, width.toFloat()
        )
        circularReveal.duration = 300
        circularReveal.start()
    }


    private fun closeSearch() {
        val circularConceal = ViewAnimationUtils.createCircularReveal(
            binding.searchOpenView,
            (binding.openSearchButton.right + binding.openSearchButton.left) / 2,
            (binding.openSearchButton.top + binding.openSearchButton.bottom) / 2,
            width.toFloat(), 0f
        )

        circularConceal.duration = 300
        circularConceal.start()
        circularConceal.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator) = Unit
            override fun onAnimationCancel(animation: Animator) = Unit
            override fun onAnimationStart(animation: Animator) = Unit
            override fun onAnimationEnd(animation: Animator) {
                binding.searchOpenView.visibility = View.INVISIBLE
                binding.searchInputText.setText("")
                circularConceal.removeAllListeners()
            }
        })
    }

}