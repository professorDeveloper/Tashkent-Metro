package com.Zbekz.tashkentmetro.utils.custom

import android.text.Editable
import android.text.TextWatcher

object SearchTextWatcher: TextWatcher {
    private var onTextChangedAction: ((String) -> Unit)? = null

    fun setOnTextChangedListener(action: (String) -> Unit) {
        onTextChangedAction = action
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }

    override fun afterTextChanged(s: Editable?) {
        onTextChangedAction?.invoke(s.toString())
    }
}