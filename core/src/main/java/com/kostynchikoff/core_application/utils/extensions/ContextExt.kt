package com.kostynchikoff.core_application.utils.extensions

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast

fun Context?.toast(msg: String) = Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()

fun Context?.hideKeyboard(view: View?) {
    try {
        val keyboard = this?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        keyboard.hideSoftInputFromWindow(view?.windowToken, 0)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun Context.showKeyboard(view: View?) {
    view?.requestFocus()
    val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
}


/**
 * Запускает вибрацию
 * @param [milliseconds] время вибрирование в миллисекундах
 */
fun Context.startVibrator(milliseconds: Long) {
    val v = getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        v?.vibrate(VibrationEffect.createOneShot(milliseconds, VibrationEffect.DEFAULT_AMPLITUDE))
    } else {
        v?.vibrate(milliseconds)
    }
}

fun Context.toSp(px: Float) = px / resources.displayMetrics.scaledDensity

/**
 * Показываем файл через стандартные средства устройства
 * @param uri файла
 * @param type файла например application/pdf
 */
fun Context.showFileInSystemDevice(uri: Uri, type: String) {
    val intent = Intent(Intent.ACTION_VIEW).apply {
        setDataAndType(uri, type)
        flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
    }
    startActivity(intent)
}
