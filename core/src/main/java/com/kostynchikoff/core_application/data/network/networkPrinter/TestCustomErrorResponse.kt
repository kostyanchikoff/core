package com.kostynchikoff.core_application.data.network.networkPrinter

import com.google.gson.Gson

class TestCustomErrorResponse : NetworkErrorPrinter<TestCustomErrorResponse> {

    var hello: String? = null
    var helloErrorResponse  : String? = null

    override fun print(response: String?, default: String): TestCustomErrorResponse = try {
        from(response).print(default)
    } catch (e: Exception) {
        TestCustomErrorResponse()
    }

    override fun from(response: String?) = Gson().fromJson(response, TestCustomErrorResponse::class.java)

    override fun print(default: String): TestCustomErrorResponse {
        return this
    }

}