package com.kostynchikoff.core_application.utils.extensions

import android.view.View
import com.kostynchikoff.core_application.ui.widget.clickListeners.SafeClickListener

/**
 * Блокировка множественного нажатия
 */
fun View.safeClickListener(block : (View) -> Unit){
    setOnClickListener(SafeClickListener(onSafeCLick = {
        block(it)
    }))
}