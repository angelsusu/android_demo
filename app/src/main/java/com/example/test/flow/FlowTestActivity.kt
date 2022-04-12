package com.example.test.flow

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.test.R
import com.example.test.commonDebug
import kotlinx.android.synthetic.main.activity_flow_test.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*

/**
 * author: beitingsu
 * created on: 2022/4/12 3:01 下午
 */
class FlowTestActivity: AppCompatActivity() {

    @InternalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flow_test)

        init()
    }

    @InternalCoroutinesApi
    private fun init() {
        btn_flow?.setOnClickListener {
            flowTest()
        }
    }

    @InternalCoroutinesApi
    private fun flowTest() {
        //flow中不可以使用withContext切换线程
        GlobalScope.launch {
            //创建flow，collect可以理解为rxjava的subscribe()，emit对应onNext()
            flow {
                for (i in 1..5) {
                    delay(100)
                    emit(i)
                }
            }.collect(object : FlowCollector<Int> {
                override suspend fun emit(value: Int) {
                    commonDebug("flow value:$value:${Thread.currentThread().name}")
                }
            })


            //切换线程,collect指定的线程取决于flow处于哪个scope下
            flow {
                for (i in 1..5) {
                    delay(100)
                    emit(i)
                }
            }.map {
                commonDebug("map value:$it:${Thread.currentThread().name}")
                it * it
            }.flowOn(Dispatchers.IO).collect(object : FlowCollector<Int> {
                override suspend fun emit(value: Int) {
                    commonDebug("flowOn value:$value:${Thread.currentThread().name}")
                }
            })

            //StateFlow
            val stateFlow = MutableStateFlow(1)
            for (i in 1..10) {
                stateFlow.emit(i)
            }
            stateFlow.collect {
                commonDebug("stateFlow:$it")
            }
        }

        val sharedFlow = MutableSharedFlow<Int>(5, 3, BufferOverflow.DROP_OLDEST)
        GlobalScope.launch {
            for (i in 1..10) {
                sharedFlow.tryEmit(i)
            }
        }

        //SharedFlow
        // 参数含义：1-》当新的订阅者collect时，发送几个已经发送过的数据。2 -》减去第一个参数，还需要缓存多少数据
        // 3 -》缓存策略
        //emit和collect不能在一个协程中
        lifecycleScope.launch {
            sharedFlow.collect {
                commonDebug("sharedFlow one:$it")
            }
        }

    }

    private fun liveDataTest() {
        //liveData和协程一起使用，有编译问题，暂不处理
//        val name = liveData(Dispatchers.IO) {
//            delay(1000)
//            emit("beiting")
//        }
//
//        name.observe(this, Observer {
//            commonDebug("liveDataTest:$it")
//        })
    }
}