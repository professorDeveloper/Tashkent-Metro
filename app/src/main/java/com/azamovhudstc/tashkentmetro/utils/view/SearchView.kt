package com.azamovhudstc.tashkentmetro.utils.view

import android.animation.Animator
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.azamovhudstc.tashkentmetro.databinding.ViewSearchBinding

class SearchView(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs) {

    private val binding: ViewSearchBinding =
        ViewSearchBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        binding.openSearchButton.setOnClickListener { openSearch() }
        binding.closeSearchButton.setOnClickListener { closeSearch() }

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