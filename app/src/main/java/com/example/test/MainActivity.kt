package com.example.test

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.dianping.logan.Logan
import com.dianping.logan.LoganConfig
import com.example.test.lifecycle.LifecycleTestActivity
import com.example.test.log.LoganParser
import com.example.test.log.TestActivity
import com.example.test.player.PlayerTestActivity
import com.example.test.room.RoomTestActivity
import com.tencent.mars.xlog.Xlog
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentLinkedQueue


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()
    }

    private fun init() {
        btn_take_photo?.setOnClickListener {
            checkPermissionAndCamera()
        }

        btn_take_video?.setOnClickListener {
            takeVideo()
        }

        btn_choose_pic?.setOnClickListener {
            checkPermissionAndChoosePic()
        }

        btn_choose_video?.setOnClickListener {
            checkPermissionAndChooseVideo()
        }
        btn_logger_logan?.setOnClickListener {
            loganTest()
        }
        btn_logger_parse?.setOnClickListener {
            loganParse()
        }
        btn_logger_xLog?.setOnClickListener {
            xLogTest()
        }
        btn_player_test?.setOnClickListener {
            startActivity(Intent(this, PlayerTestActivity::class.java))
        }
        btn_room_test?.setOnClickListener {
            startActivity(Intent(this, RoomTestActivity::class.java))
        }
        btn_lifecycle_test?.setOnClickListener {
            startActivity(Intent(this, LifecycleTestActivity::class.java))
        }
    }

    private fun openCamera() {
        val captureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(captureIntent, CAMERA_REQUEST_CODE)
    }

    private fun takeVideo() {
        val intent = Intent()
        intent.action = "android.media.action.VIDEO_CAPTURE"
        startActivityForResult(intent, 0)
    }

    private fun choosePhoto() {
        val intentToPickPic = Intent(Intent.ACTION_PICK, null)
        // 如果限制上传到服务器的图片类型时可以直接写如："image/jpeg 、 image/png等的类型" 所有类型则写 "image/*"
        intentToPickPic.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
        startActivityForResult(intentToPickPic, GALLERY_REQUEST_CODE)

    }

    private fun chooseVideo() {
        val intentToPickPic = Intent(Intent.ACTION_PICK, null)
        intentToPickPic.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "video/*")
        startActivityForResult(intentToPickPic, GALLERY_REQUEST_CODE)
    }

    private fun checkPermissionAndCamera() {
        val hasCameraPermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.CAMERA
        )
        if (hasCameraPermission == PackageManager.PERMISSION_GRANTED) {
            //有调起相机拍照。
            openCamera()
        } else {
            val array = arrayOf(Manifest.permission.CAMERA)
            //没有权限，申请权限。
            ActivityCompat.requestPermissions(
                this, array,
                PERMISSION_CAMERA_REQUEST_CODE
            )
        }
    }

    private fun checkPermissionAndChoosePic() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            == PackageManager.PERMISSION_GRANTED
        ) {
            //有权限
            choosePhoto()
        } else {
            //权限还没有授予，需要在这里写申请权限的代码
            // 第二个参数是一个字符串数组，里面是你需要申请的权限 可以设置申请多个权限
            // 最后一个参数是标志你这次申请的权限，该常量在onRequestPermissionsResult中使用到
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), GALLERY_REQUEST_CODE
            )

        }
    }

    private fun checkPermissionAndChooseVideo() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            == PackageManager.PERMISSION_GRANTED
        ) {
            //有权限
            chooseVideo()
        } else {
            //权限还没有授予，需要在这里写申请权限的代码
            // 第二个参数是一个字符串数组，里面是你需要申请的权限 可以设置申请多个权限
            // 最后一个参数是标志你这次申请的权限，该常量在onRequestPermissionsResult中使用到
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), GALLERY_REQUEST_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_CAMERA_REQUEST_CODE -> {
                if (grantResults.size > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    //允许权限，有调起相机拍照
                    openCamera()
                } else {
                    //拒绝权限，弹出提示框。
                    Toast.makeText(this, "拍照权限被拒绝", Toast.LENGTH_LONG).show()
                }
            }
            GALLERY_REQUEST_CODE -> {
                if (grantResults.size > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    //允许权限，有调起相机拍照
                    choosePhoto()
                } else {
                    //拒绝权限，弹出提示框。
                    Toast.makeText(this, "查看相册权限被拒绝", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    //美团logan测试
    private fun loganTest() {
        val dir = applicationContext.filesDir.absolutePath + "logan_v1"
        val file = File(dir)
        if (!file.exists()) {
            file.mkdirs()
        }
        //初始化
        val config = LoganConfig.Builder()
            .setCachePath(dir)
            .setPath(
                (applicationContext.getExternalFilesDir(null)?.absolutePath
                        + File.separator) + "logan" + File.separator +  "logan_v1"
            )
            .setEncryptKey16("0123456789012345".toByteArray())
            .setEncryptIV16("0123456789012345".toByteArray())
            .build()
        Logan.init(config)
        Logan.setDebug(true)
        Logan.setOnLoganProtocolStatus { cmd, code ->
            Log.d("loganTest", "$cmd:$code")
        }

        //test
        loganWrite()
    }

    //美团logan解密测试
    private fun loganParse() {
        val parser = LoganParser(
            "0123456789012345".toByteArray(),
            "0123456789012345".toByteArray()
        )
        val map = Logan.getAllFilesInfo()
        for ((key, value) in map) {
            val name = getDateToString(key)
            val fileName =
                applicationContext.getExternalFilesDir(null)?.absolutePath + File.separator + "logan_v1/" + name
            val outFileName =
                applicationContext.getExternalFilesDir(null)?.absolutePath + File.separator + "logan_v1/" + key + "_parse.txt"
            Log.d("loganTest", "$fileName")
            var inputStream: InputStream? = null
            var i = 0
            try {
                inputStream = FileInputStream(File(fileName))
                val out = FileOutputStream(File(outFileName))
                parser.parse(inputStream, out)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    fun getDateToString(str: String): Long {
        var dateFormat = SimpleDateFormat("yyyy-MM-dd");
        var date = Date();
        try {
            date = dateFormat.parse(str);
        } catch (e: ParseException) {
            e.printStackTrace();
        }
        return date.getTime()
    }

    private fun loganWrite() {
        Thread(Runnable {
            var i = 0
            while ( i < 1000) {
                Logan.w("subeiting test logan:$i", 2)
                Log.d("loganTest", "$i")
                ++i
                Thread.sleep(5)
            }
            Log.d("loganTest", "finish loganTest")
            Logan.f()
            loganParse()
        }).start()
    }

    //微信 xLog测试
    private fun xLogTest() {
        //初始化
        System.loadLibrary("c++_shared");
        System.loadLibrary("marsxlog");

        val SDCARD = getExternalFilesDir("/xlog")?.absolutePath
        val logPath = SDCARD

        // this is necessary, or may crash for SIGBUS
        val cachePath = this.getFilesDir().absolutePath + "/xlog"

        Log.d("xLogTest", "$logPath")
        Xlog.appenderOpen(Xlog.LEVEL_DEBUG, Xlog.AppednerModeAsync, cachePath, logPath,
            "MarsSample", 0, "");
        Xlog.setConsoleLogOpen(true);

        com.tencent.mars.xlog.Log.setLogImp(Xlog())

        //test
        com.tencent.mars.xlog.Log.d("xLogTest", "", "", 0, 0, 0, 0L, "test xlog")
        startActivity(Intent(this, TestActivity::class.java))

    }

    //todo 测试数据结构
    private fun testMap() {
        val map = ConcurrentHashMap<String, ConcurrentLinkedQueue<String>>()

        GlobalScope.launch {
            map.getOrPut("test") {
                ConcurrentLinkedQueue()
            }?.add("test1")
        }

        GlobalScope.launch {
            map.getOrPut("test") {
                ConcurrentLinkedQueue()
            }?.add("test2")
        }

        GlobalScope.launch {
            map.getOrPut("test") {
                ConcurrentLinkedQueue()
            }?.add("test3")
        }
        GlobalScope.launch {
            delay(3000)
            map["test"]?.forEach { str ->
                Log.d("subeiting", "test map:$str")
            }
        }
    }

    companion object {
        private const val PERMISSION_CAMERA_REQUEST_CODE = 100
        private const val CAMERA_REQUEST_CODE = 101
        private const val GALLERY_REQUEST_CODE = 102

    }
}
