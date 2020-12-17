package com.example.phototest

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.dianping.logan.Logan
import com.dianping.logan.LoganConfig
import java.io.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * author: beitingsu
 * created on: 2020/5/13 2:06 PM
 */
class TestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loganTest()
    }


    //美团logan测试
    private fun loganTest() {
        val dir = applicationContext.filesDir.absolutePath + "logan_v2"
        val file = File(dir)
        if (!file.exists()) {
            file.mkdirs()
        }
        //初始化
        val config = LoganConfig.Builder()
            .setCachePath(dir)
            .setPath(
                (applicationContext.getExternalFilesDir(null)?.absolutePath
                        + File.separator) + "logan_v2"
            )
            .setEncryptKey16("0123456789012345".toByteArray())
            .setEncryptIV16("0123456789012345".toByteArray())
            .build()
        Logan.init(config)
        Logan.setDebug(true)
        Logan.setOnLoganProtocolStatus { cmd, code ->
            Log.d("loganTest", "$cmd:$code")
        }
        loganWrite()
    }

    //美团logan解密测试
    private fun loganParse() {
        val parser = LoganParser("0123456789012345".toByteArray(), "0123456789012345".toByteArray())
        val map = Logan.getAllFilesInfo()
        for ((key, value) in map) {
            val name = getDateToString(key)
            val fileName =
                applicationContext.getExternalFilesDir(null)?.absolutePath + File.separator + "logan_v2/" + name
            val outFileName =
                applicationContext.getExternalFilesDir(null)?.absolutePath + File.separator + "logan_v2/" + key + "_parse.txt"
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
            var i = 1000
            while ( i >= 0) {
                Logan.w("subeiting test logan in other process:$i", 2)
                --i
                Log.d("loganTest", "in other process:$i")
                Thread.sleep(5)
            }
            Log.d("loganTest", "finish loganTest in other process")
            Logan.f()
            loganParse()
        }).start()
    }


}