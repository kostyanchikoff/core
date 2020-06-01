package com.kostynchikoff.core_application.utils.extensions

import android.R
import android.animation.Animator
import android.animation.ArgbEvaluator
import android.animation.StateListAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.RequiresApi
import androidx.core.graphics.drawable.DrawableCompat

/**
 * Created by Yergali Zhakhan on 5/20/20.
 */

@SuppressLint("RestrictedApi")
fun ImageView.colorAnimator(
    @ColorInt from: Int,
    @ColorInt to: Int,
    durationInMillis: Long
): Animator = ValueAnimator.ofObject(ArgbEvaluator(), from, to).apply {
    duration = durationInMillis
    addUpdateListener { animator ->
        val color = animator.animatedValue as Int
        run { setColorFilter(color) }
    }
}

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
fun ImageView.setColorStateListAnimator(
    @ColorInt color: Int,
    @ColorInt unselectedColor: Int
) {
    val stateList = StateListAnimator().apply {
        addState(
            intArrayOf(R.attr.state_selected),
            colorAnimator(unselectedColor, color, 350)
        )
        addState(
            intArrayOf(),
            colorAnimator(color, unselectedColor, 350)
        )
    }

    stateListAnimator = stateList

    // Refresh the drawable state to avoid the unselected animation on view creation
    refreshDrawableState()
}

var DURATION = 150L
var ALPHA = 1.0f
fun TextView.expand(container: LinearLayout, iconColor: Int, itemWidth: Int) {
    val bounds = Rect()
    val bubbleWidth = if(itemWidth > 0) itemWidth else bounds.width() + paddingLeft + 10
    container.setCustomBackground(iconColor, ALPHA)
    paint.apply {

        getTextBounds(text.toString(), 0, text.length, bounds)
        ValueAnimator.ofInt(0, bubbleWidth).apply {
            addUpdateListener {
                if (it.animatedFraction == (0.0f)) {
                    visibility = View.INVISIBLE
                }
                layoutParams.apply {
                    width = it.animatedValue as Int
                }

                if (it.animatedFraction == (1.0f)) {
                    visibility = View.VISIBLE
                }

                requestLayout()
            }
            interpolator = LinearInterpolator()

            duration = DURATION
        }.start()
    }
}


fun TextView.collapse(
    container: LinearLayout,
    iconColor: Int
) {
    animate().alpha(0f).apply {
        setUpdateListener {
            layoutParams.apply {
                width = (width - (width * it.animatedFraction)).toInt()
            }
            if (it.animatedFraction == 1.0f) {
                visibility = View.GONE
                alpha = 1.0f
            }
            interpolator = LinearInterpolator()
            duration = DURATION
            container.setCustomBackground(iconColor, ALPHA - (ALPHA * it.animatedFraction))
            requestLayout()
        }
    }.start()

}

fun View.setCustomBackground(color: Int, alpha: Float) {
    val containerBackground = GradientDrawable().apply {
        cornerRadius = 100f
        DrawableCompat.setTint(
            this,
            Color.argb(
                (Color.alpha(color) * alpha).toInt(),
                Color.red(color),
                Color.green(color),
                Color.blue(color)
            )
        )
    }
    background = containerBackground
}