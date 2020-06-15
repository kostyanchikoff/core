package com.kostynchikoff.core_application.utils.extensions

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.lifecycle.viewModelScope
import com.kostynchikoff.core_application.data.network.ResultApi
import com.kostynchikoff.core_application.data.network.Status
import com.kostynchikoff.core_application.presentation.model.UIValidation
import com.kostynchikoff.core_application.presentation.viewModel.CoreViewModel
import com.kostynchikoff.core_application.utils.wrappers.EventWrapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.net.ssl.HttpsURLConnection

fun <T : Any> CoreViewModel.launch(
    block: suspend () -> ResultApi<T>,
    result: (T?) -> Unit,
    errorBlock: ((String) -> Unit?)? = null
) {
    viewModelScope.launch(Dispatchers.Main) {
        statusLiveData.value = Status.SHOW_LOADING
        val value = block()
        unwrap(value, {
            result(it)
        }, errorBlock)
    }
}

fun <T : Any> CoreViewModel.unwrap(
    result: ResultApi<T>,
    successBlock: (T?) -> Unit,
    errorBlock: ((String) -> Unit?)? = null
) {
    when (result) {
        is ResultApi.Success -> {
            successBlock(result.data)
            statusLiveData.value = Status.HIDE_LOADING
        }
        is ResultApi.HttpError -> {
            /**
             * Если лямбда для обработки ошибки не определена
             * Тогда выводим ошибку в liveData
             *
             * Бывают случаи когда ошибку нужно обработать
             */
            if (errorBlock == null) {
                _errorLiveData.value = EventWrapper(result.error)
            } else {
                errorBlock.invoke(result.error)
            }

            /**
             * Если приходит код 401 и ты имеем токен
             * отправляем в стутус редирект в экран логина или запрос нового токена
             */
            if (result.code == HttpsURLConnection.HTTP_UNAUTHORIZED && !getPref().getAccessToken()
                    .isNullOrEmpty()
            ) {
                statusLiveData.value = Status.REDIRECT_LOGIN
                return
            }

            /**
             * В случае ошибки сервера получаем статус ошибки
             */
            statusLiveData.value = Status.HIDE_LOADING
            statusLiveData.value = Status.ERROR
        }
    }
}

/**
 * Выводим сообщение об ошибке
 */
fun CoreViewModel.showError(msg: String) {
    _errorLiveData.value = EventWrapper(msg)
}


/**
 * Перенаправление на нужный фрагмент
 * @param [action] отправляем id action-а который приписываеться в nav графе
 * @param [msg] сообщение может быть любого характера
 */

fun CoreViewModel.redirectToFragment(@IdRes action: Int, bundle: Bundle? = null) {
    _redirectFragment.value = Pair(action, bundle)
}

/**
 * Выводим сообщение для определенного поля
 * @param errorMessage сообщение которые показываем на UI
 * @param type тип например Type.password (задаем в текущем модуле для определенного поля)
 */
fun CoreViewModel.showErrorByType(errorMessage: String, type: String) {
    _errorByType.value = UIValidation(errorMessage, type)
}
