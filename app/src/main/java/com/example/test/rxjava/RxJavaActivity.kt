package com.example.test.rxjava

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.test.R
import kotlinx.android.synthetic.main.activity_rxjava_test.*


/**
 * author: beitingsu
 * created on: 2021/3/25 2:45 PM
 */
class RxJavaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rxjava_test)

        init()
    }

    private fun init() {
        btn_create_test?.setOnClickListener {
            startActivity(Intent(this, RxJavaCreateActivity::class.java))
        }
        btn_transform_test?.setOnClickListener {
            startActivity(Intent(this, RxJavaTransformActivity::class.java))
        }
        btn_combine_test?.setOnClickListener {
            startActivity(Intent(this, RxJavaCombineActivity::class.java))
        }
    }
}