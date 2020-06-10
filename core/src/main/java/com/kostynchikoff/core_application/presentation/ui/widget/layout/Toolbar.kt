package com.kostynchikoff.core_application.presentation.ui.widget.layout

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.DrawableRes
import com.kostynchikoff.core_application.R


class Toolbar : RelativeLayout {

    private @DrawableRes
    var leftIcon: Int? = null

    private @DrawableRes
    var rightIcon: Int? = null
    private var leftIconVisibility: Boolean? = null
    private var rightIconVisibility: Boolean? = null
    private var showBackIcon: Boolean? = null
    private var title: String? = null

    private var titleTv: TextView? = null
    private var leftIconIv: ImageView? = null
    private var rightIconIv: ImageView? = null

    private var mContext: Context? = null

    constructor(context: Context?) : this(context, null)

    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {

        mContext = context
        initializeView(attrs, defStyleAttr)
    }

    private fun initializeView(attrs: AttributeSet?, defStyleAttr: Int) {
        if (mContext == null) return

        if (attrs != null && !isInEditMode) {
            val attributes: TypedArray = mContext?.theme?.obtainStyledAttributes(
                attrs, R.styleable.Toolbar,
                defStyleAttr, 0
            ) ?: return

            leftIcon =
                attributes.getResourceId(R.styleable.Toolbar_leftIcon, 0)
            rightIcon =
                attributes.getResourceId(R.styleable.Toolbar_rightIcon, 0)
            title =
                attributes.getString(R.styleable.Toolbar_titleToolbar)
            leftIconVisibility =
                attributes.getBoolean(R.styleable.Toolbar_leftIconVisibility, false)
            rightIconVisibility =
                attributes.getBoolean(R.styleable.Toolbar_rightIconVisibility, false)
            showBackIcon =
                attributes.getBoolean(R.styleable.Toolbar_showBackIcon, false)
            attributes.recycle()


            val inflater =
                mContext?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val view =
                inflater.inflate(R.layout.toolbar_view, this) as Toolbar
            leftIconIv =
                view.findViewById(R.id.leftIconBv) as ImageView
            titleTv =
                view.findViewById(R.id.titleTv) as TextView
            rightIconIv =
                view.findViewById(R.id.rightIconBv) as ImageView


            if (!title.isNullOrEmpty()) {
                setTitle(title!!)
            }

            if (rightIcon != null && rightIcon != 0) {
                setRightIcon(rightIcon!!)
            }

            if (showBackIcon == true) {
                showBackButton()
            } else {
                if (leftIcon != null && leftIcon != 0) {
                    setLeftIcon(leftIcon!!)
                }
            }
        }
    }

    fun getRightIcon(): ImageView? {
        return rightIconIv
    }

    fun getLeftIcon(): ImageView? {
        return leftIconIv
    }

    fun setTitle(text: String) {
        title = text
        titleTv?.let {
            it.text = text
        }
    }

    fun setRightIcon(icon: Int) {
        rightIcon = icon
        rightIconIv?.let {
            it.setImageResource(icon)
            it.visibility = View.VISIBLE
        }
    }


    fun doOnClickLeftIcon(block: () -> Unit) {
        leftIconIv?.setOnClickListener {
            block()
        }
    }

    fun doOnClickRigthIcon(block: () -> Unit) {
        rightIconIv?.setOnClickListener {
            block()
        }
    }

    fun showBackButton() {
        leftIconIv?.visibility = View.VISIBLE
    }

    fun setLeftIcon(icon: Int) {
        this.leftIcon = icon
        leftIconIv?.let { iconTv ->
            leftIcon?.let {
                iconTv.setImageResource(it)
            }
            iconTv.visibility = View.VISIBLE
        }
    }
}