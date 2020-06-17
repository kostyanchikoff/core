package com.kostynchikoff.core_application.utils.delegates

import androidx.lifecycle.Observer
import com.kostynchikoff.core_application.data.network.Status
import com.kostynchikoff.core_application.presentation.model.UIValidation
import com.kostynchikoff.core_application.utils.wrappers.EventObserver


interface LiveDataEvent {
    /**
     * LiveData для стутусов при ожидании ответа с сервера
     */
    val statusObserver: Observer<Status>

    /**
     * LiveData для ошибок, ошибки приходят с [CoreViewModel]
     */
    val errorMessageObserver: EventObserver<String>

    /**
     * LiveData для ошибок с конкретным типом, ошибки приходят с [CoreViewModel]
     */
    val errorMessageByTypeObserver: EventObserver<UIValidation>


    fun showLoader(){
        // do nothing
    }

    fun hideLoader(){
        // do nothing
    }

    fun error(msg : String){
        // do nothing
    }

    fun errorByType(msg : String, type : String){
        // do nothing
    }

    fun success(){
        // do nothing
    }

    fun redirectLogin(){
        // do nothing
    }

}

class LiveDataEventDelegate : LiveDataEvent{

    override val statusObserver = Observer<Status> {
        it?.let {
            when (it) {
                Status.SHOW_LOADING -> showLoader()
                Status.HIDE_LOADING -> hideLoader()
                Status.REDIRECT_LOGIN -> redirectLogin()
                Status.SUCCESS -> success()
                else -> return@let
            }
        }
    }

    override val errorMessageObserver = EventObserver<String> {
        // do nothing
    }

    override val errorMessageByTypeObserver = EventObserver<UIValidation> {

    }
}

