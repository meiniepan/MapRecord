package com.solang.maprecord.widgets

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import com.solang.maprecord.R
import kotlinx.android.synthetic.main.layout_title_bar.view.*

/**
 * @author Burning
 * @description:
 * @date :2020/8/12 7:38 PM
 */
class CustomTitleBar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    private var title: String?
    private var isWhiteIcon: Boolean = false

    init {
        val typedArray =
            getContext().obtainStyledAttributes(attrs, R.styleable.CustomTitleBar)
        title = typedArray.getString(R.styleable.CustomTitleBar_title)
        isWhiteIcon = typedArray.getBoolean(R.styleable.CustomTitleBar_white_icon, false)
        typedArray.recycle()
        init()
    }

    private fun init() {
        View.inflate(context, R.layout.layout_title_bar, this)
        iv_back_custom.setOnClickListener {
            if (context is Activity) {
                (context as Activity).finish()
            } else if (context is Fragment) {
                (context as Fragment).activity?.finish()
            }
        }
        title?.let {
            tv_title_custom.visibility = View.VISIBLE
            tv_title_custom.text = title
        }
        if (isWhiteIcon) {
            tv_title_custom.setTextColor(resources.getColor(R.color.white));
            iv_back_custom.setImageResource(R.mipmap.icon_back_white_a)
        }

    }
}