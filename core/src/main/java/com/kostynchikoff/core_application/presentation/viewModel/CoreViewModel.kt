package com.kostynchikoff.core_application.presentation.viewModel

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kostynchikoff.core_application.data.network.Status
import com.kostynchikoff.core_application.presentation.model.UIValidation
import com.kostynchikoff.core_application.utils.delegates.EncryptedPrefDelegate
import com.kostynchikoff.core_application.utils.delegates.EncryptedPrefDelegateImpl
import com.kostynchikoff.core_application.utils.wrappers.EventWrapper
import org.koin.core.KoinComponent

abstract class CoreViewModel : ViewModel(), KoinComponent,
    EncryptedPrefDelegate by EncryptedPrefDelegateImpl() {

    /**
     * Вывод ошибки при обрабоке http запросов
     */
    internal val _errorLiveData = MutableLiveData<EventWrapper<String>>()
    val errorLiveData: LiveData<EventWrapper<String>>
        get() = _errorLiveData


    /**
     * Статус при http запросе
     */
    val statusLiveData = MutableLiveData<Status>()

    /**
     * Вывод ошибок для конкретного поля
     */
    internal val _errorByType = MutableLiveData<UIValidation>()
    val errorByType: LiveData<UIValidation>
        get() = _errorByType

    internal val _redirectFragment = MutableLiveData<Pair<@IdRes Int, Bundle?>>()
    val redirectFragment: LiveData<Pair<Int, Bundle?>>
        get() = _redirectFragment
}