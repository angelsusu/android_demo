package com.example.test.widgets

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.ColorInt
import com.example.test.R
import kotlinx.android.synthetic.main.circle_progress_bar_view.view.*

/**
 * author: beitingsu
 * created on: 2020/12/21 5:29 PM
 * 圆形进度组件，包含圆形view、中心文字、中心icon三部分
 */
class CircleProgressBar : FrameLayout {

    companion object {
        private const val TAG = "CircleProgressBar"
    }

    private var mText = ""
    private var mTextColor = Color.parseColor("#FA9025")
    private var mTextSize = 12f

    private var mIconType = 0


    constructor(context: Context) : super(context) {
        init(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        init(attrs, defStyle)
    }

    private fun init(attrs: AttributeSet?, defStyle: Int) {
        val view = View.inflate(context, R.layout.circle_progress_bar_view, null)
        addView(
            view,
            ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        )

        // Load attributes
        val array = context.obtainStyledAttributes(
            attrs, R.styleable.CircleProgressBar, defStyle, 0
        )
        mText = array.getString(R.styleable.CircleProgressBar_text) ?: ""
        mTextColor = array.getColor(R.styleable.CircleProgressBar_textColor, mTextColor)
        mTextSize = array.getDimension(R.styleable.CircleProgressBar_textFontSize, mTextSize)
        mIconType = array.getInt(R.styleable.CircleProgressBar_iconType, mIconType)

        //setText(mText)
        setTextSize(mTextSize)
        setTextColor(mTextColor)
        setIconType(mIconType)

        array.recycle()
    }

    /**
     * 设置文本
     */
    fun setText(text: String) {
        tv_circle_bar_text?.text = text
        tv_circle_bar_text?.visibility = View.VISIBLE
        iv_circle_bar_icon?.visibility = View.GONE
        Log.d("CircleProgressBar", "setIcon visibility Gone")
    }

    /**
     * 设置文本字体大小
     */
    fun setTextSize(size: Float) {
        tv_circle_bar_text?.textSize = size
    }

    /**
     * 设置文本颜色
     */
    fun setTextColor(@ColorInt color: Int) {
        tv_circle_bar_text?.setTextColor(color)
    }

    /**
     * 设置文本字体
     */
    fun setTypeface(@RobotoTypefaces.RobotoTypeface typefaces: Int) {
        tv_circle_bar_text?.setRobotoTypeface(typefaces)
    }

    /**
     * 设置中心icon
     */
    fun setIconType(iconType: Int) {
        getIconRes(iconType)?.let { res ->
            iv_circle_bar_icon?.visibility = View.VISIBLE
            tv_circle_bar_text?.visibility = View.GONE
            iv_circle_bar_icon?.setImageResource(res)
            Log.d("CircleProgressBar", "setIcon visibility visible")
        }
    }

    private fun getIconRes(iconType: Int): Int? {
        return when (iconType) {
            CircleProgressIconType.ICON_SUCCESS -> R.drawable.icon_incentives_success
            else -> null
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        Log.d(TAG, "onMeasure")
    }
}

object CircleProgressIconType {
    const val ICON_SUCCESS = 1
}