package com.kostynchikoff.core_application.data.constants

import android.app.Activity

object CoreVariables {

    /**
     * Id фрагмента во всем приложении
     */
    var ID_FRAGMENT = -1

    /**
     * url для REST API
     */
    var BASE_URL = ""

    /**
     * url для graphQL
     */
    var BASE_APOLLO_URL = ""

    /**
     * Activity для авторизации
     */
    var LOGIN_ACTIVITY: Activity? = null

    /**
     * Для хранение url-ов которые не нуждаються в token
     */
    var URLS_OF_UNNECESSARY_BEARER_TOKEN_ENDPOINTS: List<String> = emptyList()

    /**
     * Базовые header для refresh token
     */
    var BASIC_REFRESH_AUTH_HEADER = ""

    /**
     * Запрос для refresh token
     */
    var REFRESH_TOKEN_END_POINT = ""

    /**
     * Тип оператора связи
     */
    var OPERATOR = ""


}