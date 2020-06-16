package com.kostynchikoff.core_application.data.network.networkPrinter

interface NetworkErrorPrinter<T> {

    fun print(response: String?, default: String) : T

    fun from(response: String?) : NetworkErrorPrinter<T>

    fun print(default: String) : T
}
