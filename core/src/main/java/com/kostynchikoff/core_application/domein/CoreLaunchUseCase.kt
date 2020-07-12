package com.kostynchikoff.core_application.domein

import com.kostynchikoff.core_application.utils.delegates.CoreCoroutine
import com.kostynchikoff.core_application.utils.delegates.CoreCoroutineDelegate

/**
 * В случае если нужно отловить запрос в UseCase и преобразовать данный необходимо воспользоваться данным классом
 * @param T параметр метода
 * @param V результат выполненного действия
 */
abstract class CoreLaunchUseCase<in T, out V> : CoreCoroutine by CoreCoroutineDelegate(){

    abstract fun execute(param : T, result : ((V) -> Unit))
}