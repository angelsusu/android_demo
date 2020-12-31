package com.example.test.widgets

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.test.R
import kotlinx.android.synthetic.main.activity_widgets_test.*

/**
 * author: beitingsu
 * created on: 2020/12/21 10:56 AM
 */
class WidgetsTestActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_widgets_test)

        init()
    }

    private fun init() {
        btn_change_text?.setOnClickListener {
            circle_bar?.setText("go go go")
        }
        btn_change_icon?.setOnClickListener {
            circle_bar?.setIconType(CircleProgressIconType.ICON_SUCCESS)
        }
    }
}