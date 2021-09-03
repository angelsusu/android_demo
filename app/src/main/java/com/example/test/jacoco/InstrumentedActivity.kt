package com.example.test.jacoco

import android.os.Bundle
import android.util.Log
import com.example.test.MainActivity

/**
 * author: beitingsu
 * created on: 2021/7/12 3:54 下午
 */
class InstrumentedActivity : MainActivity() {
    private var mListener: FinishListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("InstrumentedActivity", "onCreate()")
    }

    fun setFinishListener(listener: FinishListener?) {
        mListener = listener
    }

    override fun onDestroy() {
        Log.d("InstrumentedActivity", "onDestroy()")
        super.onDestroy()
        mListener?.onActivityFinished()
    }
}