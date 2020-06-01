package com.kostynchikoff.core_application.utils.os

/**
 * Получаем минуты с миллисекунд
 */
fun getMinute(time : Long) = time / 1000 / 60

/**
 * Получаем секунды с миллисекунд
 */
fun getSecond(time : Long) = (time / 1000 % 60)