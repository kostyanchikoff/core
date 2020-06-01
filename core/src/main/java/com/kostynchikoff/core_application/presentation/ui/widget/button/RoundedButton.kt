package com.kostynchikoff.core_application.presentation.ui.widget.button

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.util.AttributeSet
import android.widget.Button
import androidx.core.content.ContextCompat
import com.kostynchikoff.core_application.R


class RoundedButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = R.attr.buttonStyle
) : Button(context, attrs, defStyleAttr) {
    private var mRadius = 20f
    private var color = R.color.colorPrimary

    init {
        val ta =
            context.obtainStyledAttributes(attrs, R.styleable.RoundedButton, 0, 0)
        try {
            mRadius = ta.getFloat(R.styleable.RoundedButton_radius, 20f)
            color = ta.getResourceId(R.styleable.RoundedButton_color, R.color.colorPrimary)
        } finally {
            ta.recycle()
        }
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        createRoundedBackground(canvas?.width, canvas?.height)
    }

    private fun createRoundedBackground(width: Int?, height: Int?) {

        val widthNotNull = width ?: 0
        val heightNotNull = height ?: 0
        val bitmap = Bitmap.createBitmap(
            widthNotNull, heightNotNull, Bitmap.Config.ARGB_8888
        )

        val canvas = Canvas(bitmap)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.style = Paint.Style.FILL
        paint.isAntiAlias = true
        paint.color = ContextCompat.getColor(context,  color)
        val rectF = RectF(
            0f,
            0f,
            canvas.width.toFloat(),
            canvas.height.toFloat()
        )
        canvas.drawRoundRect(
            rectF,
            mRadius,
            mRadius,
            paint
        )

        val drawable = BitmapDrawable(bitmap)
        background = drawable
    }
}