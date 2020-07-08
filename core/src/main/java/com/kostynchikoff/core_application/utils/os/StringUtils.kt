package com.kostynchikoff.core_application.utils.os

import com.kostynchikoff.core_application.data.constants.CoreConstant
import com.kostynchikoff.core_application.data.constants.CorePatternConstant.PATTERN_FORMAT_AMOUNT_SPACE
import java.text.DecimalFormat
import java.text.NumberFormat

/**
 * Формирование из строки в числовое значение с двумя нулями
 */
fun formatWithZeroAmount(value: String?, currency: String = CoreConstant.EMPTY): String {
    if(value.isNullOrEmpty()){
        return "0.00"
    }
    val formatter: NumberFormat = DecimalFormat(PATTERN_FORMAT_AMOUNT_SPACE)
    return "${formatter.format(value.toDouble()).replace(",", " ")} $currency"
}