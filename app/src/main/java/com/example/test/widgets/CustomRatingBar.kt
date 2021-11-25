package com.example.test.widgets

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatRatingBar

/**
 * author: beitingsu
 * created on: 2021/11/3 11:16 上午
 */
class CustomRatingBar: AppCompatRatingBar {

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(
        context,
        attrs,
        android.R.attr.ratingBarStyle
    )

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )


    //todo
    override fun setRating(rating: Float) {
//        var finalrating = rating
//        val xx = rating * 10  % 10
//        if ( xx > 0 && xx < 5) {
//            finalrating += 0.5f
//        }
        progress = (rating * getProgressPerStar()).toInt()
    }

    private fun getProgressPerStar(): Float {
        return if (numStars > 0) {
            1f * max / numStars
        } else {
            1f
        }
    }
}