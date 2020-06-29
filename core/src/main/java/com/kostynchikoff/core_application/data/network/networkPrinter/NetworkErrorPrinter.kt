package com.kostynchikoff.core_application.data.network.networkPrinter

interface NetworkErrorHttpPrinter<T> {

    fun print(response: String?, default: String?): T
}
