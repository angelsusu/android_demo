package com.example.test.ipc

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.RemoteCallbackList
import com.example.test.IServiceCallback
import com.example.test.IServiceInterface
import com.example.test.data.BookInfo

/**
 * author: beitingsu
 * created on: 2021/4/23 3:28 PM
 */
class IPCService: Service() {

    //管理远程回调列表
    private val mCallbackList = RemoteCallbackList<IServiceCallback>()

    private val mBinder = object : IServiceInterface.Stub() {
        override fun getBookInfo(): BookInfo {
            return BookInfo("subeiting", 23.56)
        }

        override fun registListener(listener: IServiceCallback?) {
            mCallbackList.register(listener)

            //notify listener
            notifyListeners()
        }

        override fun unregistListener(listener: IServiceCallback?) {
            mCallbackList.unregister(listener)
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return mBinder
    }

    private fun notifyListeners() {
        for (index in 0 until mCallbackList.beginBroadcast()) {
            val listener = mCallbackList.getBroadcastItem(index)
            listener?.callback()
        }
        mCallbackList.finishBroadcast()
    }
}