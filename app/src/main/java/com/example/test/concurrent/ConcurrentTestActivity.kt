package com.example.test.concurrent

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.test.Common
import com.example.test.R
import com.example.test.commonDebug
import com.example.test.debug
import kotlinx.android.synthetic.main.activity_concurrent_test.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.CountDownLatch
import java.util.concurrent.CyclicBarrier
import java.util.concurrent.Semaphore
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.locks.ReentrantLock
import java.util.concurrent.locks.ReentrantReadWriteLock
import java.util.concurrent.locks.StampedLock
import kotlin.concurrent.withLock

/**
 * author: beitingsu
 * created on: 2021/4/27 2:14 PM
 */
class ConcurrentTestActivity : AppCompatActivity() {

    private var mSynchronizedValue = 0

    @Volatile
    private var mVolatileValue = false

    private var mCASValue = AtomicBoolean(false)

    //初始化计数器总量为2
    private val mCountDownLatch = CountDownLatch(2)

    //初始化5个信号量
    private val mSemaphore = Semaphore(5)

    //默认非公平锁
    private val mReentrantLock = ReentrantLock()

    //读写锁，默认非公平锁，可能会发生写饥饿
    private val mReentrantReadWriteLock = ReentrantReadWriteLock()
    private val mReadLock = mReentrantReadWriteLock.readLock()
    private val mWriteLock = mReentrantReadWriteLock.writeLock()

    //解决了写饥饿的问题
    @RequiresApi(Build.VERSION_CODES.N)
    private val mStampedLock = StampedLock()
    private var mStampedValue = 0

    //设置屏障要拦截的线程为2, 处理完之后执行当前run方法
    private var mCyclicBarrier = CyclicBarrier(2) {
        commonDebug("cyclicBarrier await end")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_concurrent_test)

        init()
    }

    private fun init() {
        btn_synchronized?.setOnClickListener {
            synchronizedTest()
        }
        btn_volatile?.setOnClickListener {
            volatileTest()
        }
        btn_cas?.setOnClickListener {
            casTest()
        }
        btn_countDownLatch?.setOnClickListener {
            countDownLatchTest()
        }
        btn_semaphore?.setOnClickListener {
            semaphoreTest()
        }
        btn_reentrantLock?.setOnClickListener {
            //reentrantLockTest1()
            reentrantLockTest2()
        }
        btn_reentrantReadWriteLock?.setOnClickListener {
            reentrantReadWriteLockTest()
        }
        btn_stampedLock?.setOnClickListener {
            stampedLockTest()
        }
        btn_cyclicBarrier?.setOnClickListener {
            cyclicBarrierTest()
        }
    }

    private fun synchronizedTest() {
        GlobalScope.launch {
            synchronizedFun()
        }
        GlobalScope.launch {
            synchronizedFun()
        }
    }

    @Synchronized
    private fun synchronizedFun() {
        for (index in 1..10) {
            mSynchronizedValue++
            debug(Common.TAG, "synchronized test:$mSynchronizedValue")
        }
    }

    private fun volatileTest() {
        GlobalScope.launch {
            mVolatileValue = true
            debug(Common.TAG, "volatile test in thread 1:$mVolatileValue")
        }
        GlobalScope.launch {
            debug(Common.TAG, "volatile test in thread 2:$mVolatileValue")
        }
    }

    private fun casTest() {
        GlobalScope.launch {
            val result = mCASValue.compareAndSet(false, true)
            debug(Common.TAG, "cas test in thread 1:$result")
        }
        GlobalScope.launch {
            val result = mCASValue.compareAndSet(false, true)
            debug(Common.TAG, "cas test in thread 2:$result")
        }
    }

    private fun countDownLatchTest() {
        //线程被阻塞
        GlobalScope.launch {
            kotlin.runCatching {
                mCountDownLatch.await()
                debug(Common.TAG, "countDownLatch test await in thread 1")
            }
        }
        //线程被阻塞
        GlobalScope.launch {
            kotlin.runCatching {
                mCountDownLatch.await()
                debug(Common.TAG, "countDownLatch test await in thread 2")
            }
        }
        GlobalScope.launch {
            kotlin.runCatching {
                delay(1000)
                mCountDownLatch.countDown()
                debug(Common.TAG, "countDownLatch test countDown in thread 3")
            }
        }
        GlobalScope.launch {
            kotlin.runCatching {
                delay(2000)
                mCountDownLatch.countDown()
                debug(Common.TAG, "countDownLatch test countDown in thread 4")
            }
        }
    }

    private fun semaphoreTest() {
        GlobalScope.launch {
            kotlin.runCatching {
                mSemaphore.acquire(2)
                debug(Common.TAG, "semaphore test acquire in thread 1")
            }
        }
        GlobalScope.launch {
            kotlin.runCatching {
                mSemaphore.acquire(2)
                debug(Common.TAG, "semaphore test acquire in thread 2")
                delay(1000)

                //释放一个信号量
                mSemaphore.release(1)
                debug(Common.TAG, "semaphore test release in thread 2")
            }
        }
        //信号量不够，会被阻塞
        GlobalScope.launch {
            kotlin.runCatching {
                debug(Common.TAG, "semaphore test in thread 3")
                mSemaphore.acquire(2)
                debug(Common.TAG, "semaphore test acquire in thread 3")
            }
        }
    }

    //协程应该使用Mutex，这段测试大概率会导致无法解锁成功
    //因为delay后resume的线程不是之前lock的线程，所以无法进行解锁
    private fun reentrantLockTest1() {
        GlobalScope.launch {
            kotlin.runCatching {
                val isSuccess = mReentrantLock.tryLock()
                debug(Common.TAG, "reentrantLock test in thread 1:$isSuccess:${Thread.currentThread().name}")

                delay(1000)
                debug(Common.TAG, "reentrantLock test unlock in thread 1 begin:${Thread.currentThread().name}")
                mReentrantLock.unlock()
                debug(Common.TAG, "reentrantLock test unlock in thread 1 end")
            }
        }
        GlobalScope.launch {
            kotlin.runCatching {
                var isSuccess = mReentrantLock.tryLock(1, TimeUnit.SECONDS)
                debug(Common.TAG, "reentrantLock test in thread 2:$isSuccess")

                delay(3000)
                isSuccess = mReentrantLock.tryLock(1, TimeUnit.SECONDS)
                debug(Common.TAG, "reentrantLock test tryLock one more in thread 2:$isSuccess")
            }
        }
    }

    private fun reentrantLockTest2() {
        Thread(Runnable {
            val isSuccess = mReentrantLock.tryLock()
            debug(
                Common.TAG,
                "reentrantLock test in thread 1:$isSuccess:${Thread.currentThread().name}"
            )

            Thread.sleep(1000)
            debug(
                Common.TAG,
                "reentrantLock test unlock in thread 1 begin:${Thread.currentThread().name}"
            )
            mReentrantLock.unlock()
            debug(Common.TAG, "reentrantLock test unlock in thread 1 end")
        }).start()

        Thread(Runnable {
            var isSuccess = mReentrantLock.tryLock(1, TimeUnit.SECONDS)
            debug(Common.TAG, "reentrantLock test in thread 2:$isSuccess")

            Thread.sleep(1000)
            isSuccess = mReentrantLock.tryLock(1, TimeUnit.SECONDS)
            debug(Common.TAG, "reentrantLock test tryLock one more in thread 2:$isSuccess")
        }).start()
    }

    private fun reentrantReadWriteLockTest() {
        Thread(Runnable {
            mReadLock.lock()
            debug(
                Common.TAG,
                "reentrantReadWriteLock test read lock in thread 1:${Thread.currentThread().name}"
            )
            Thread.sleep(1000)
            mReadLock.unlock()
            debug(
                Common.TAG,
                "reentrantReadWriteLock test read lock in thread 1 end"
            )
        }).start()

        Thread(Runnable {
            mReadLock.lock()
            debug(
                Common.TAG,
                "reentrantReadWriteLock test read lock in thread 2:${Thread.currentThread().name}"
            )
            Thread.sleep(1000)
            mReadLock.unlock()
            debug(
                Common.TAG,
                "reentrantReadWriteLock test read lock in thread 2 end"
            )
        }).start()

        Thread(Runnable {
            mWriteLock.withLock {
                debug(
                    Common.TAG,
                    "reentrantReadWriteLock test write lock in thread 2:${Thread.currentThread().name}"
                )
            }
        }).start()
    }

    private fun stampedLockTest() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            return
        }
        Thread(Runnable {
            val lock = mStampedLock.tryOptimisticRead()  //乐观读锁
            debug(
                Common.TAG,
                "stampedLock test read lock in thread 1:$mStampedValue"
            )
            Thread.sleep(1000)
            //检查在获取读锁期间是否有写入
            if (!mStampedLock.validate(lock)) {
                val readLock = mStampedLock.readLock() //获取一个悲观读锁
                debug(
                    Common.TAG,
                    "stampedLock test read lock one more in thread 1:$mStampedValue"
                )
                mStampedLock.unlockRead(readLock)  //释放悲观读锁
            }
            debug(
                Common.TAG,
                "stampedLock test read lock in thread 1 end"
            )
        }).start()
        Thread(Runnable {
            val lock = mStampedLock.writeLock()
            mStampedValue = 2
            debug(
                Common.TAG,
                "stampedLock test write lock in thread 2:$mStampedValue"
            )
            Thread.sleep(1000)
            mStampedLock.unlockWrite(lock)
            debug(
                Common.TAG,
                "stampedLock test write lock in thread 2 end"
            )
        }).start()
    }

    private fun cyclicBarrierTest() {
        Thread(Runnable {
            debug(
                Common.TAG,
                "cyclicBarrier test in thread 1 begin"
            )
            //线程完成工作后调用await设置屏障
            mCyclicBarrier.await()
            debug(
                Common.TAG,
                "cyclicBarrier test in thread 1 end"
            )
        }).start()

        Thread(Runnable {
            debug(
                Common.TAG,
                "cyclicBarrier test in thread 2 begin"
            )
            //线程完成工作后调用await设置屏障
            mCyclicBarrier.await()
            debug(
                Common.TAG,
                "cyclicBarrier test in thread 2 end"
            )
        }).start()
    }

}