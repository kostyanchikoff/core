package com.kostynchikoff.core_application.utils


import com.google.gson.JsonParseException
import com.kostynchikoff.core_application.data.network.ResultApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.EOFException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLHandshakeException

class NetworkUtilsTest {



    @Test
    fun `safeApiCall SocketTimeoutException test`(){
        runBlocking {
            assertEquals (safeApiCall {
                throw SocketTimeoutException()
            }, ResultApi.HttpError("Сервер не отвечает"))
        }
    }

    @Test
    fun `safeApiCall SSLHandshakeException test`(){
        runBlocking {
            assertEquals (safeApiCall {
                throw SSLHandshakeException("")
            }, ResultApi.HttpError("Возникли проблемы с сертификатом"))
        }
    }

    @Test
    fun `safeApiCall JsonParseException test`(){
        runBlocking {
            assertEquals (safeApiCall {
                throw JsonParseException("")
            }, ResultApi.HttpError("Ошибка обработки запроса"))
        }
    }

    @Test
    fun `safeApiCall EOFException test`(){
        runBlocking {
            assertEquals (safeApiCall {
                throw EOFException("")
            }, ResultApi.HttpError("Ошибка загрузки, попробуйте еще раз"))
        }
    }

    @Test
    fun `safeApiCall ConnectException test`(){
        runBlocking {
            assertEquals (safeApiCall {
                throw ConnectException("")
            }, ResultApi.HttpError("Возникли проблемы с интернетом"))
        }
    }

    @Test
    fun `safeApiCall UnknownHostException test`(){
        runBlocking {
            assertEquals (safeApiCall {
                throw UnknownHostException("")
            }, ResultApi.HttpError("Возникли проблемы с интернетом"))
        }
    }

    @Test
    fun `safeApiCall Another test`(){
        runBlocking {
            assertEquals (safeApiCall {
                 "3r352352gdsgs" as Int
            }, ResultApi.HttpError("Ошибка : ClassCastException java.lang.String cannot be cast to java.lang.Integer"))
        }
    }

}