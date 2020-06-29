package com.kostynchikoff.core_application.utils.os

import com.kostynchikoff.core_application.data.constants.CorePatternConstant.PATTERN_FORMAT_ANOUNT_SPACE_ZERO
import java.text.DecimalFormat

/**
 * Формирование из строки в числовое значение с двумя нулями
 */
fun formatWithZeroAmount(value: String): String {

    if(value.length <= 1){
        return value
    }
    val decimalFormat = DecimalFormat(PATTERN_FORMAT_ANOUNT_SPACE_ZERO)
    val format = decimalFormat.format(value.replace(" ", "").toDouble())
    return format.replace(",", ".")
}