package com.kostynchikoff.core_application.utils.extensions

import androidx.fragment.app.DialogFragment
import com.kostynchikoff.core_application.utils.interfaces.DialogFragmentEventListener

/**
 * Для передачи данный из DialogFragment в родитеьский фрагмент
 * @param value значение которое отправляете
 * @param isClose стоит ли закрывать фрагмент после отправки события
 * @param fragment фрагмент в который хотите отправить события
 *
 */
fun DialogFragment.sendEventToFragment(
    value: String,
    isClose: Boolean = false,
    vararg fragments: String?

) {
    fragments.forEach { fragment ->
        val listener =
            fragmentManager?.fragments?.find { it::class.simpleName == fragment } as? DialogFragmentEventListener
        listener?.dialogEvent(value)
    }
    if (isClose) {
        dismiss()
    }
}