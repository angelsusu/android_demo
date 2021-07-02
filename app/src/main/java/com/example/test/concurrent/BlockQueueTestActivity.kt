package com.example.test.concurrent

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.test.Common
import com.example.test.R
import com.example.test.debug
import kotlinx.android.synthetic.main.activity_block_queue_test.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.*
import kotlin.random.Random


/**
 * author: beitingsu
 * created on: 2021/4/27 2:14 PM
 */
class BlockQueueTestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_block_queue_test)

        init()
    }

    private fun init() {
        btn_array_block_queue?.setOnClickListener {
            testArrayBlockQueue()
        }
        btn_linked_block_queue?.setOnClickListener {
            testLinkedBlockingQueue()
        }
        btn_PriorityBlockingQueue?.setOnClickListener {
            testPriorityBlockingQueue()
        }
        btn_DelayQueue?.setOnClickListener {
            testDelayQueue()
        }
    }

    //有界阻塞队列，内部使用一把锁控制，两个condition
    private fun testArrayBlockQueue() {
        val queue = ArrayBlockingQueue<Int>(10)
        GlobalScope.launch {
            kotlin.runCatching {
                debug(Common.TAG, "testArrayBlockQueue take in thread 1 begin")
                //没有数据会阻塞
                delay(1000)
                val value = queue.take()
                debug(Common.TAG, "testArrayBlockQueue take in thread 1 end:$value")
            }
        }
        GlobalScope.launch {
            kotlin.runCatching {
                debug(Common.TAG, "testArrayBlockQueue put in thread 2 begin")
                for (index in 0..10) {
                    queue.put(index) //满了也会阻塞
                    debug(Common.TAG, "testArrayBlockQueue put in thread 2 success:$index")
                }
                debug(Common.TAG, "testArrayBlockQueue put in thread 2 end")
            }
        }
    }

    //有界阻塞队列，内部使用两把锁控制, 各自的condition
    private fun testLinkedBlockingQueue() {
        val queue = LinkedBlockingQueue<Int>(10)
        GlobalScope.launch {
            kotlin.runCatching {
                debug(Common.TAG, "testLinkedBlockingQueue take in thread 1 begin")
                //没有数据会阻塞
                delay(1000)
                val value = queue.take()
                debug(Common.TAG, "testLinkedBlockingQueue take in thread 1 end:$value")
            }
        }
        GlobalScope.launch {
            kotlin.runCatching {
                debug(Common.TAG, "testLinkedBlockingQueue put in thread 2 begin")
                for (index in 0..10) {
                    queue.put(index) //满了也会阻塞
                    debug(Common.TAG, "testLinkedBlockingQueue put in thread 2 success:$index")
                }
                debug(Common.TAG, "testLinkedBlockingQueue put in thread 2 end")
            }
        }
    }

    //无界阻塞优先级队列，内部使用一把锁+一个condition
    private fun testPriorityBlockingQueue() {
        //优先级大返回<0 否则返回>0
        val queue = PriorityBlockingQueue<Student>(10) { o1, o2 ->
            if (o1.age > o2.age) -1 else 1
        }
        GlobalScope.launch {
            kotlin.runCatching {
                debug(Common.TAG, "testPriorityBlockingQueue take in thread 1 begin")
                //没有数据会阻塞
                val value = queue.take()
                debug(
                    Common.TAG,
                    "testPriorityBlockingQueue take in thread 1 end:$${value.name}:${value.age}"
                )
            }
        }
        GlobalScope.launch {
            kotlin.runCatching {
                val random = Random(1000)
                debug(Common.TAG, "testPriorityBlockingQueue put in thread 2 begin")
                for (index in 0..10) {
                    queue.put(Student("Name_$index", random.nextInt(100))) //不会阻塞
                    debug(Common.TAG, "testPriorityBlockingQueue put in thread 2 success:$index")
                }
                debug(Common.TAG, "testPriorityBlockingQueue put in thread 2 end")
                for (index in 0 until queue.size) {
                    val student = queue.take()
                    debug(Common.TAG, "testPriorityBlockingQueue:${student.name}:${student.age}")
                }
            }
        }
    }

    //无界队列，延迟队列，按延迟优先级排序。在PriorityQueue基础上实现的
    private fun testDelayQueue() {
        val queue = DelayQueue<Student>()
        GlobalScope.launch {
            kotlin.runCatching {
                debug(Common.TAG, "testDelayQueue take in thread 1 begin")
                //没有数据会阻塞
                queue.take()
                debug(Common.TAG, "testDelayQueue take in thread 1 end")
            }
        }
        GlobalScope.launch {
            kotlin.runCatching {
                val random = Random(1000)
                val now = System.currentTimeMillis()
                debug(Common.TAG, "testDelayQueue put in thread 2 begin")
                for (index in 0..10) {
                    queue.put(Student("Name_$index", random.nextInt(100), now + 1000)) //不会阻塞
                    debug(Common.TAG, "testDelayQueue put in thread 2 success:$index")
                }
                debug(Common.TAG, "testDelayQueue put in thread 2 end")
            }
        }
    }
}

class Student(val name: String, val age: Int, val time: Long = 0) : Delayed {
    override fun compareTo(other: Delayed?): Int {
        return if (age > (other as Student).age) -1 else 1
    }

    //会delay之后再次调用 不是只调用一次 需要时间《=0 才可以取出
    override fun getDelay(unit: TimeUnit): Long {
        return unit.convert(time - System.currentTimeMillis(), TimeUnit.MILLISECONDS)
    }
}