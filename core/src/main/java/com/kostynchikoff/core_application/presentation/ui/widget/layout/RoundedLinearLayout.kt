package com.kostynchikoff.core_application.presentation.ui.widget.layout

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.kostynchikoff.core_application.R
import com.kostynchikoff.core_application.utils.extensions.drawRoundedBg

class RoundedLinearLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var mRadius = 20f
    private var color = R.color.colorPrimary

    init {
        val ta =
            context.obtainStyledAttributes(attrs, R.styleable.RoundedLinearLayout, 0, 0)
        try {
            mRadius = ta.getDimension(R.styleable.RoundedLinearLayout_radius, 20f)
            color = ta.getResourceId(R.styleable.RoundedLinearLayout_color, R.color.colorPrimary)
        } finally {
            ta.recycle()
        }
        setWillNotDraw(false)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawRoundedBg(ContextCompat.getColor(context, color), mRadius, this)
    }

}