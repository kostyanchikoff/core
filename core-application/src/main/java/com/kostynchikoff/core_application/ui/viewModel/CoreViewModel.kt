package com.kostynchikoff.core_application.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kostynchikoff.core_application.data.network.Status
import com.kostynchikoff.core_application.ui.model.UIValidation
import com.kostynchikoff.core_application.utils.delegates.EncryptedPrefDelegate
import com.kostynchikoff.core_application.utils.delegates.EncryptedPrefDelegateImpl
import kotlinx.coroutines.CoroutineScope
import org.koin.core.KoinComponent

abstract class CoreViewModel : ViewModel(), KoinComponent,
    EncryptedPrefDelegate by EncryptedPrefDelegateImpl() {

    /**
     * Вывод ошибки при обрабоке http запросов
     */
    val errorLiveData = MutableLiveData<String>()

    /**
     * Статус при http запросе
     */
    val statusLiveData = MutableLiveData<Status>()

    /**
     * Вывод ошибок для валидации полей
     */
    internal val _validation = MutableLiveData<UIValidation>()
    val validation: LiveData<UIValidation>
        get() = _validation
}