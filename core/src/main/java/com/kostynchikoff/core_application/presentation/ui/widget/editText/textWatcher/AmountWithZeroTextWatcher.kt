package com.kostynchikoff.core_application.presentation.ui.widget.editText.textWatcher

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import com.kostynchikoff.core_application.data.constants.CoreConstant
import com.kostynchikoff.core_application.data.constants.CorePatternConstant.PATTERN_FORMAT_ANOUNT_SPACE_ZERO
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.ParseException


class AmountWithZeroTextWatcher(private val et: EditText, private val block : (String) -> Unit) : TextWatcher {
    private val decimalFormatSymbol = DecimalFormatSymbols().apply {
        groupingSeparator = ' '
    }
    private val formatter: DecimalFormat = DecimalFormat(PATTERN_FORMAT_ANOUNT_SPACE_ZERO, decimalFormatSymbol).apply {
        maximumFractionDigits = 2
        maximumIntegerDigits = 10
    }

    override fun afterTextChanged(s: Editable) {
        et.removeTextChangedListener(this)
        try {
            val srt = s.toString().replace(" ", "")
            val value = formatter.parse(srt)

            var startCursor =  et.selectionStart
            val afterCursorSymbol = formatter.format(value).length

            val length =   formatter.format(value).length

            val selector =  if(startCursor >= afterCursorSymbol-1 && startCursor <= length){
                startCursor++
            }else{
                formatter.format(value).length-3
            }

            var text = formatter.format(value)

            if(length <= 3){
                text = CoreConstant.EMPTY
            }
            et.setText(text)
            et.setSelection(selector)
        } catch (nfe: NumberFormatException) {
            // do nothing?
        } catch (e: ParseException) {
            // do nothing?
        }
        et.addTextChangedListener(this)
    }

    override fun beforeTextChanged(
        s: CharSequence,
        start: Int,
        count: Int,
        after: Int
    ) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        block(s?.toString().orEmpty())
    }

}