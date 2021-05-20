package com.example.test.rxjava

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.test.R
import com.example.test.commonDebug
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_rxjava_test.*


/**
 * author: beitingsu
 * created on: 2021/3/25 2:45 PM
 */
class RxJavaActivity : AppCompatActivity() {

    private val mDisposables = CompositeDisposable()

    companion object {
        private const val UI_TOPIC = 1
        private const val IO_TOPIC = 2
    }

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
        btn_bus_test?.setOnClickListener {
            Bus.push(Event(UI_TOPIC, "this is ui topic"))
            Bus.push(Event(IO_TOPIC, "this is io topic"))
        }

        mDisposables.add(Bus.monitorTopicEventOnUI(UI_TOPIC).subscribe { event ->
            commonDebug("${event.topic}:${event.data}")
        })
        mDisposables.add(Bus.monitorTopicEventBackground(IO_TOPIC).subscribe { event ->
            commonDebug("${event.topic}:${event.data}")
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        mDisposables.clear()
    }
}