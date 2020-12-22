package com.example.test.widgets

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.example.test.R


/**
 * Created by subeiting on 2019/1/16
 */
class CircleProgressView : View {

    private var mHeight: Int = 0
    private var mWidth: Int = 0
    private lateinit var mPaint: Paint
    private var strokeWidth: Float = 5f//线条宽度
    private var rectF = RectF()
    private var normalColor = Color.parseColor("#A5A5A5")//普通的颜色
    private var progressColor = Color.parseColor("#FA9025")//已经走了的进度条颜色
    private var completeColor =  Color.parseColor("#FA9025")

    private var sweepAngle = 0f//进度条角度
    private var progress_style = Paint.Style.STROKE//填充式还是环形式
    private val FULL_PERCENT = 100f
    private val START_ANGLE = 270f
    private val CIRCLE_FULL_ANGLE = 360f

    private var mIsNeedAnim = false


    private val anim by lazy {
        ValueAnimator.ofFloat(0f, sweepAngle)?.apply {
            addUpdateListener {
                sweepAngle = it.animatedValue as Float
                Log.d("CircleProgressBar", "${it.animatedValue}")
                invalidate()
            }
        }
    }

    constructor(context: Context) : super(context) {
        init(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init(attrs, defStyle)
    }

    private fun init(attrs: AttributeSet?, defStyle: Int) {
        // Load attributes
        val array = context.obtainStyledAttributes(
                attrs, R.styleable.CircleProgressView, defStyle, 0)
        strokeWidth = array.getDimension(R.styleable.CircleProgressView_strokeWidth, strokeWidth)
        normalColor = array.getColor(R.styleable.CircleProgressView_circleLineBackColor, normalColor)
        progressColor = array.getColor(R.styleable.CircleProgressView_circleLineFrontColor, progressColor)
        completeColor = array.getColor(R.styleable.CircleProgressView_circleLineCompleteColor, completeColor)
        val progress = array.getFloat(R.styleable.CircleProgressView_progress, sweepAngle)
        sweepAngle = CIRCLE_FULL_ANGLE * progress / FULL_PERCENT
        val animDuration = array.getInt(R.styleable.CircleProgressView_animationDuration, 0)
        mIsNeedAnim = animDuration != 0
        array.recycle()

        initPaint()

    }

    // 2.初始化画笔
    private fun initPaint() {
        mPaint = Paint()
        mPaint.color = normalColor       //设置画笔颜色
        mPaint.isAntiAlias = true
        mPaint.style = Paint.Style.STROKE  //设置画笔模式为描边
        mPaint.strokeWidth = strokeWidth
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        mHeight = MeasureSpec.getSize(heightMeasureSpec)
        mWidth = MeasureSpec.getSize(widthMeasureSpec)

        if (mHeight > mWidth) {
            rectF.set(strokeWidth, mHeight / 2 - mWidth / 2 + strokeWidth, mWidth - strokeWidth, mHeight / 2 + mWidth / 2 - strokeWidth)
        } else if (mWidth > mHeight) {
            //宽大于高的情况
            rectF.set(mWidth / 2 - mHeight / 2 + strokeWidth, strokeWidth, mWidth / 2 + mHeight / 2 - strokeWidth, mHeight - strokeWidth)
        } else {
            //宽等于高的情况
            rectF.set(strokeWidth, strokeWidth, mWidth - strokeWidth, mHeight - strokeWidth)
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (mIsNeedAnim) {
            startRingAnimation(1000)
            mIsNeedAnim = false
        } else {
            //画普通圆
            mPaint.color = normalColor
            canvas.drawArc(rectF, START_ANGLE, CIRCLE_FULL_ANGLE,
                false, mPaint)

            if (sweepAngle == CIRCLE_FULL_ANGLE) {
                mPaint.color = completeColor
            } else {
                mPaint.color = progressColor
            }
            mPaint.strokeCap = Paint.Cap.ROUND  //圆角
            canvas.drawArc(rectF, START_ANGLE, sweepAngle, false, mPaint)
        }
    }

    /**
     * 更新界面
     * @param progress
     */
    fun updateProgress(progress: Float) {
        this.sweepAngle = CIRCLE_FULL_ANGLE * progress / FULL_PERCENT
        postInvalidate()
    }

    //进度动画
    private fun startRingAnimation(duration: Long) {
        anim?.duration = duration
        anim?.start()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        anim?.cancel()
    }
}
