package com.kostynchikoff.core_application.utils.extensions

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.drawable.BitmapDrawable
import android.view.View
import androidx.annotation.ColorInt


/**
 * @param color цвет фона округленной фигуры
 * @param radius радиус скругления
 * @param view текущая view
 */
fun Canvas.drawRoundedBg(@ColorInt color : Int, radius : Float, view : View){
    val widthNotNull = width
    val heightNotNull = height
    val bitmap = Bitmap.createBitmap(
        widthNotNull, heightNotNull, Bitmap.Config.ARGB_8888
    )

    val canvas = Canvas(bitmap)
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    paint.style = Paint.Style.FILL
    paint.isAntiAlias = true
    paint.color = color
    val rectF = RectF(
        0f,
        0f,
        canvas.width.toFloat(),
        canvas.height.toFloat()
    )
    canvas.drawRoundRect(
        rectF,
        radius,
        radius,
        paint
    )

    val drawable = BitmapDrawable(bitmap)
    view.background = drawable
}