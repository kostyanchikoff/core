package com.kostynchikoff.core_application.utils.extensions

import android.text.Editable
import android.text.TextWatcher
import android.widget.TextView

fun TextView.doOnTextChange(block: (CharSequence?) -> Any) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(p0: Editable?) {
            // do nothing
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            // do nothing
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            block(p0)
        }

    })
}