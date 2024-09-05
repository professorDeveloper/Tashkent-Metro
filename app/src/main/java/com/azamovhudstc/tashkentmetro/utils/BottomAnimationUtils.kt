package com.azamovhudstc.tashkentmetro.utils

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.TypedValue
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.azamovhudstc.tashkentmetro.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.card.MaterialCardView


fun BottomNavigationView.showWithAnimation(fragmentContainerView: View) {
    if (this.visibility == View.VISIBLE) return
    this.visible()
    this.animateTranslationY(0f, 66f, 700)
    fragmentContainerView.animateMarginBottom(66f, 700)
}

fun View.visible() {
    this.visibility = View.VISIBLE
}


fun View.gone() {
    this.visibility = View.GONE
}

fun MaterialCardView.select() {
    this.strokeWidth = 5
    this.strokeColor = ContextCompat.getColor(context, R.color.colorPrimary)
}


fun MaterialCardView.unSelect() {
    this.strokeWidth = 0
//    this.strokeColor = ContextCompat.getColor(context, R.color.colorPrimary)
}


fun View.animateTranslationY(animateFrom: Float, animateTo: Float, duration: Long) {
    val animator =
        ObjectAnimator.ofFloat(
            this, "translationY", TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                animateTo,
                resources.displayMetrics
            ), TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                animateFrom,
                resources.displayMetrics
            )
        )
    animator.duration = duration
    if (animateTo == 0f) {
        animator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                this@animateTranslationY.gone()
            }
        })
    }
    animator.start()

}


fun View.animateMarginBottom(size: Float, duration: Long) {
    val dpToPx = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        size,
        resources.displayMetrics
    )


    val params =
        this.layoutParams as ConstraintLayout.LayoutParams
    val animator = ValueAnimator.ofInt(params.bottomMargin, dpToPx.toInt())
    animator.addUpdateListener {
        val value = it.animatedValue as Int
        params.setMargins(
            params.leftMargin,
            params.topMargin,
            params.rightMargin,
            value
        )
        this.layoutParams = params
    }
    animator.duration = duration
    animator.start()
}
