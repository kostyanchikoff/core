package com.kostynchikoff.core_application.domein

import com.kostynchikoff.core_application.data.network.ResultApi

interface CoreUseCase<in I, out V : Any> {

   suspend fun execute(param: I): ResultApi<V>
}