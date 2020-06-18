package com.kostynchikoff.core_application.presentation.ui.widget.button

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.widget.Button
import androidx.core.content.ContextCompat
import com.kostynchikoff.core_application.R
import com.kostynchikoff.core_application.utils.extensions.drawRoundedBg


class RoundedButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = R.attr.buttonStyle
) : Button(context, attrs, defStyleAttr) {
    private var mRadius = 20f
    private var color = android.R.color.black

    init {
        val ta =
            context.obtainStyledAttributes(attrs, R.styleable.RoundedButton, 0, 0)
        try {
            mRadius = ta.getDimension(R.styleable.RoundedButton_radius, 20f)
            color = ta.getResourceId(R.styleable.RoundedButton_color, android.R.color.black)
        } finally {
            ta.recycle()
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawRoundedBg(ContextCompat.getColor(context, color), mRadius, this)
    }
}