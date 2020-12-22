package com.example.test.widgets

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

/**
 * @author vianhuang
 * @date 2020/11/19 2:23 PM
 */
class RobotoTextView : AppCompatTextView {
    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(
        context,
        attrs,
        android.R.attr.textViewStyle
    )

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        if (!isInEditMode) {
            RobotoTypefaces.setUpTypeface(
                this,
                context,
                attrs
            )
        }
    }

    /**
     * 设置roboto字体
     */
    fun setRobotoTypeface(@RobotoTypefaces.RobotoTypeface typeface: Int): Boolean {
        return if (!isInEditMode) {
            RobotoTypefaces.setUpTypeface(this, typeface)
            true
        } else false
    }
}

