package com.example.test.activityresult

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.test.R
import kotlinx.android.synthetic.main.activity_result_test.*


/**
 * author: beitingsu
 * created on: 2021/3/25 2:45 PM
 */
class ResultActivity : AppCompatActivity() {

    companion object {
        const val INPUT_DATA_KEY = "data"
        const val OUTPUT_DATA_KEY = "data"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result_test)

        tv_test?.text = intent?.getStringExtra(INPUT_DATA_KEY) ?: "default"
    }

    //setResult需要在finish之前调用
    override fun finish() {
        setResult(Activity.RESULT_OK, Intent().apply {
            putExtra(OUTPUT_DATA_KEY, "back from ResultActivity")
        })
        super.finish()
    }
}