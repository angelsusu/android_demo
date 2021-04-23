package com.example.test.ipc

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.appcompat.app.AppCompatActivity
import com.example.test.*
import kotlinx.android.synthetic.main.activity_ipc_test.*

/**
 * author: beitingsu
 * created on: 2021/1/9 10:34 AM
 */
class IPCTestActivity : AppCompatActivity() {

    private var mService: IPCService? = null
    private var mServiceConnection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
            debug(Common.TAG, "onServiceDisconnected")
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            mInterface = IServiceInterface.Stub.asInterface(service)
            debug(Common.TAG, "onServiceConnected:${mInterface?.bookInfo}")
            mInterface?.registListener(mCallback)
        }
    }

    private var mInterface: IServiceInterface? = null
    private val mCallback = object : IServiceCallback.Stub() {
        override fun callback() {
            debug(Common.TAG, "onServiceCallback")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_ipc_test)

        init()
    }

    private fun init() {
        btn_aidl?.setOnClickListener {
            bindIPCService()
        }
    }

    private fun bindIPCService() {
        if (mService == null) {
            bindService(
                Intent(this, IPCService::class.java),
                mServiceConnection,
                Context.BIND_AUTO_CREATE
            )
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        mInterface?.unregistListener(mCallback)
        mService?.unbindService(mServiceConnection)
        mService?.stopSelf()
    }

}