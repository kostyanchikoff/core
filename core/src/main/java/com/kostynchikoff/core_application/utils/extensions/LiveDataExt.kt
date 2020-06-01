package com.kostynchikoff.core_application.utils.extensions

import androidx.lifecycle.*
import com.kostynchikoff.core_application.utils.event.OneTimeObserver

/**
 * Расширения используеться для тестирования liveData
 * В определенном жизненом цикле
 */
fun <T> LiveData<T>.observeOnce(onChangeHandler: (T) -> Unit) {
    val observer = OneTimeObserver(handler = onChangeHandler)
    observe(observer, observer)
}

