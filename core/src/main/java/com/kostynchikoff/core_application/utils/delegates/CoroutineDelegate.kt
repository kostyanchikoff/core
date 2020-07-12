package com.kostynchikoff.core_application.utils.delegates

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kostynchikoff.core_application.data.constants.CoreConstant
import com.kostynchikoff.core_application.data.network.ResultApi
import com.kostynchikoff.core_application.data.network.Status
import com.kostynchikoff.core_application.presentation.model.UIValidation
import com.kostynchikoff.core_application.utils.wrappers.EventWrapper
import kotlinx.coroutines.*
import javax.net.ssl.HttpsURLConnection


interface CoreCoroutine {

    /**
     * Вывод ошибки при обрабоке http запросов
     */
    val errorEventLiveData: LiveData<EventWrapper<String>>

    /**
     * Статус при http запросе
     */
    val statusEventLiveData: LiveData<Status>

    /**
     * Вывод ошибок для конкретного поля
     */
    val errorEventByTypeLiveData: LiveData<EventWrapper<UIValidation>>


    val redirectEventFragment: LiveData<Pair<Int, Bundle?>>

    /**
     * запуск coroutine
     * Дает возможноть принимать свой тип в ответе ошибки
     * @param block suspend функция
     * @param result результат
     * @param errorBlock блок ошибки (по умолчанию String)
     * Если нужно использовать свой тив в ошибке применяем  [launchWithError]
     */
    fun <T : Any> launch(
        block: suspend () -> ResultApi<T>,
        result: (T?) -> Unit,
        errorBlock: ((String?) -> Unit?)? = null,
        dispatcher: CoroutineDispatcher = Dispatchers.Main
    )


    /**
     * запуск coroutine c применением пользовательской ошибки с бэка
     * Дает возможноть принимать свой тип в ответе ошибки
     * @param block suspend функция
     * @param result результат
     * @param errorBlock блок ошибки (передаем свой тип)
     */
    fun <T : Any, V : Any> launchWithError(
        block: suspend () -> ResultApi<T>,
        result: (T?) -> Unit,
        errorBlock: ((V?) -> Unit?)? = null,
        dispatcher: CoroutineDispatcher = Dispatchers.Main
    )


    /**
     * Обработчик ошибок для viewModel используеться для того чтобы обработать ошибку с пользовательской model
     * @param result результат
     * @param successBlock получание результата (возврашает тип)
     * @param errorBlock получание ошибки (Возврашает тип переданный в [launchWithError])
     */
    fun <T : Any, V : Any> unwrapWithError(
        result: ResultApi<T>,
        successBlock: (T?) -> Unit,
        errorBlock: ((V) -> Unit?)? = null
    )


    /**
     * запуск coroutine c применением пользовательской ошибки с бэка
     * Дает возможноть принимать свой тип в ответе ошибки
     * @param block suspend функция
     * @param result результат
     * @param errorBlock блок ошибки с типом String
     */
    fun <T : Any> unwrap(
        result: ResultApi<T>,
        successBlock: (T?) -> Unit,
        errorBlock: ((String) -> Unit?)? = null
    )


    /**
     * Выводим сообщение об ошибке
     */
    fun showError(msg: String)

    /**
     * Перенаправление на нужный фрагмент
     * @param [action] отправляем id action-а который приписываеться в nav графе
     * @param [msg] сообщение может быть любого характера
     */
    fun redirectToFragment(@IdRes action: Int, bundle: Bundle? = null)

    /**
     * Выводим сообщение для определенного поля
     * @param errorMessage сообщение которые показываем на UI
     * @param type тип например Type.password (задаем в текущем модуле для определенного поля)
     */
    fun showErrorByType(errorMessage: String?, type: String?)

    /**
     * Очиска coroutine (если спользовать в паре с CoreLaunchViewModel
     * или CoreAndroidLaunchViewModel тогда отчистка происходит автоматически)
     */
    fun clearCoroutine()

}

class CoreCoroutineDelegate : CoreCoroutine, EncryptedPrefDelegate by EncryptedPrefDelegateImpl() {

    private val parentJob = Job()
    private val scope = CoroutineScope(Dispatchers.Main + parentJob)


    private val _statusEventLiveData = MutableLiveData<Status>()
    override val statusEventLiveData: LiveData<Status>
        get() = _statusEventLiveData


    private val _errorEventLiveData = MutableLiveData<EventWrapper<String>>()
    override val errorEventLiveData: LiveData<EventWrapper<String>>
        get() = _errorEventLiveData


    private val _errorEventByTypeLiveData = MutableLiveData<EventWrapper<UIValidation>>()
    override val errorEventByTypeLiveData: LiveData<EventWrapper<UIValidation>>
        get() = _errorEventByTypeLiveData


    private val _redirectEventFragment = MutableLiveData<Pair<Int, Bundle?>>()
    override val redirectEventFragment: LiveData<Pair<Int, Bundle?>>
        get() = _redirectEventFragment


    override fun <T : Any> launch(
        block: suspend () -> ResultApi<T>,
        result: (T?) -> Unit,
        errorBlock: ((String?) -> Unit?)?,
        dispatcher: CoroutineDispatcher
    ) {
        scope.launch(dispatcher) {
            _statusEventLiveData.value = Status.SHOW_LOADING
            val value = block()
            unwrap(value, {
                result(it)
            }, errorBlock)
        }
    }

    override fun <T : Any, V : Any> launchWithError(
        block: suspend () -> ResultApi<T>,
        result: (T?) -> Unit,
        errorBlock: ((V?) -> Unit?)?,
        dispatcher: CoroutineDispatcher
    ) {
        scope.launch(dispatcher) {
            _statusEventLiveData.value = Status.SHOW_LOADING
            val value = block()
            unwrapWithError(value, {
                result(it)
            }, errorBlock)
        }

    }

    override fun <T : Any, V : Any> unwrapWithError(
        result: ResultApi<T>,
        successBlock: (T?) -> Unit,
        errorBlock: ((V) -> Unit?)?
    ) {
        when (result) {
            is ResultApi.Success -> {
                _statusEventLiveData.value = Status.HIDE_LOADING
                successBlock(result.data)
            }
            is ResultApi.HttpError<*> -> {
                val error = (result.error as? V) ?: return
                errorBlock?.invoke(error)

                /**
                 * Если приходит код 401 и ты имеем токен
                 * отправляем в стутус редирект в экран логина или запрос нового токена
                 */
                if (result.code == HttpsURLConnection.HTTP_UNAUTHORIZED && !getPref().getAccessToken()
                        .isNullOrEmpty()
                ) {
                    _statusEventLiveData.value = Status.REDIRECT_LOGIN
                    return
                }

                /**
                 * В случае ошибки сервера получаем статус ошибки
                 */
                _statusEventLiveData.value = Status.HIDE_LOADING
                _statusEventLiveData.value = Status.ERROR
            }
        }
    }

    override fun <T : Any> unwrap(
        result: ResultApi<T>,
        successBlock: (T?) -> Unit,
        errorBlock: ((String) -> Unit?)?
    ) {
        when (result) {
            is ResultApi.Success -> {
                _statusEventLiveData.value = Status.HIDE_LOADING
                successBlock(result.data)
            }
            is ResultApi.HttpError<*> -> {
                /**
                 * Если лямбда для обработки ошибки не определена
                 * Тогда выводим ошибку в liveData
                 *
                 * Бывают случаи когда ошибку нужно обработать
                 */
                val error = result.error as? String ?: CoreConstant.EMPTY
                if (errorBlock == null) {
                    _errorEventLiveData.value = EventWrapper(error)
                } else {
                    errorBlock.invoke(error)
                }

                /**
                 * Если приходит код 401 и ты имеем токен
                 * отправляем в стутус редирект в экран логина или запрос нового токена
                 */
                if (result.code == HttpsURLConnection.HTTP_UNAUTHORIZED && !getPref().getAccessToken()
                        .isNullOrEmpty()
                ) {
                    _statusEventLiveData.value = Status.REDIRECT_LOGIN
                    return
                }

                /**
                 * В случае ошибки сервера получаем статус ошибки
                 */
                _statusEventLiveData.value = Status.HIDE_LOADING
                _statusEventLiveData.value = Status.ERROR
            }
        }
    }

    override fun showError(msg: String) {
        _errorEventLiveData.value = EventWrapper(msg)
    }

    override fun showErrorByType(errorMessage: String?, type: String?) {
        _errorEventByTypeLiveData.value =
            EventWrapper(UIValidation(type.orEmpty(), errorMessage.orEmpty()))

    }

    override fun redirectToFragment(action: Int, bundle: Bundle?) {
        _redirectEventFragment.value = Pair(action, bundle)
    }

    override fun clearCoroutine() {
        parentJob.cancelChildren()
    }

}