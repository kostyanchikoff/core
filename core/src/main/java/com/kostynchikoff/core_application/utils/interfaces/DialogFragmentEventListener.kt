package com.kostynchikoff.core_application.utils.interfaces

/**
 * Отслеживание событий из DialogFragment которые были отправлены через функцию расширения [sendEventToFragment]
 */
interface DialogFragmentEventListener {

    fun dialogEvent(event: String)
}

