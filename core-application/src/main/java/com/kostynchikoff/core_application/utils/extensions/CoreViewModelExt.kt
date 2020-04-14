package com.kostynchikoff.core_application.utils.extensions

import androidx.lifecycle.viewModelScope
import com.kostynchikoff.core_application.data.network.ResultApi
import com.kostynchikoff.core_application.data.network.Status
import com.kostynchikoff.core_application.ui.viewModel.CoreViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.net.ssl.HttpsURLConnection

fun <T : Any> CoreViewModel.launch(
    block: suspend () -> ResultApi<T>,
    result: (T) -> Unit,
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
    successBlock: (T) -> Unit,
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
                errorLiveData.value = result.error
            } else {
                errorBlock.invoke(result.error)
            }

            /**
             * Если приходит код 401 и ты имеем токен
             * отправляем в стутус редирект в экран логина или запрос нового токена
             */
            if (result.code == HttpsURLConnection.HTTP_UNAUTHORIZED && !getPref().getToken().isNullOrEmpty()) {
                statusLiveData.value = Status.REDIRECT_LOGIN
                return
            }

            /**
             * В случае ошибки сервера получаем статус ошибки
             */
            statusLiveData.value = Status.ERROR
        }
    }
}

