package com.kostynchikoff.core_application.utils.callback

/**
 * Получаем базовые события с LiveData для отображения загрузчика и вывода ошибок
 */
interface ResultLiveDataHandler {

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
}