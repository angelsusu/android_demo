package com.example.test.player

import android.content.Context
import android.content.res.AssetFileDescriptor
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.SoundPool
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.test.R
//import com.google.android.exoplayer2.Player
//import com.google.android.exoplayer2.SimpleExoPlayer
//import com.google.android.exoplayer2.source.ProgressiveMediaSource
//import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
//import com.google.android.exoplayer2.util.Util
import com.tencent.rtmp.ITXLivePlayListener
import com.tencent.rtmp.TXLiveConstants
import com.tencent.rtmp.TXLivePlayer
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_player_test.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import tv.danmaku.ijk.media.player.IjkMediaPlayer
import java.io.IOException
import java.util.concurrent.TimeUnit


class PlayerTestActivity : AppCompatActivity() {

    private val TAG = "PlayerTest"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player_test)

        init()
    }

    private fun init() {
        btn_SoundPool?.setOnClickListener {
            playBySoundPool()
        }
        btn_MediaPlayer?.setOnClickListener {
            playByMediaPlayer()
        }
        btn_IjkMediaPlayer?.setOnClickListener {
            playByIjkMediaPlayer()
        }
        btn_ExoPlayer?.setOnClickListener {
           // playByExoPlayer()
        }
        btn_TXPlayer?.setOnClickListener {
            playByTXPlayer()
        }
    }

    private fun playBySoundPool() {
        //初始化soundPool,设置可容纳12个音频流，音频流的质量为5，
        val soundPool = SoundPool(12, AudioManager.STREAM_MUSIC, 0)
        val id = soundPool.load(this, R.raw.hands2, 0)
        val duration = getMp3Duration(R.raw.hands2)
        val volume = getStreamCurrentVolume(this, AudioManager.STREAM_NOTIFICATION)
        soundPool.setOnLoadCompleteListener { soundPool, id, status ->
            if (status == 0) {
                Log.d(TAG, "playBySoundPool start play:$duration")
                soundPool.play(id, 1.0f,1.0f, 0, 0, 1.0f)
                GlobalScope.launch {
                    delay(duration.toLong())
                    Log.d(TAG, "playBySoundPool complete")
                    soundPool.release()
                }
            }
        }

        //getAssets().openFd("wav/"+files[i])
        //soundPool.release()
    }

    private fun playByMediaPlayer() {
        val mediaPlayer = MediaPlayer.create(this, R.raw.hands2)  //no need call prepare
//        mediaPlayer.reset()
//        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            mediaPlayer.start()
        }
        mediaPlayer.setOnCompletionListener {
            Log.d(TAG, "playByMediaPlayer complete")
            mediaPlayer.release()
        }
    }

    private fun playByIjkMediaPlayer() {
        val assetFileDescriptor: AssetFileDescriptor = assets.openFd("hands.mp3")
        val mediaPlayer = IjkMediaPlayer()
        mediaPlayer.reset()
        mediaPlayer.setDataSource(assetFileDescriptor.fileDescriptor)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            mediaPlayer.start()
        }
        mediaPlayer.setOnSeekCompleteListener {
            Log.d(TAG, "playByIjkMediaPlayer complete")
            mediaPlayer.release()
        }
    }

//    private fun playByExoPlayer() {
//        val player = SimpleExoPlayer.Builder(this).build()
//        val defaultDataSourceFactory = DefaultDataSourceFactory(this,  Util.getUserAgent(this, "MyApplication"), null)
//        val mediaSource1 = ProgressiveMediaSource.Factory(defaultDataSourceFactory)
//            .createMediaSource(Uri.parse("asset:///hands.mp3"))
//        player.prepare(mediaSource1)
//        player.playWhenReady = true
//        player.addListener(object : Player.EventListener {
//            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
//                if (playbackState == Player.STATE_ENDED) {
//                    Log.d(TAG, "playByExoPlayer complete")
//                    player.release()
//                }
//            }
//        })
//    }

    private fun playByTXPlayer() {
        val player = TXLivePlayer(this)
        player.startPlay("asset:///hands.mp3", TXLivePlayer.PLAY_TYPE_LOCAL_VIDEO)
        player.setPlayListener(object: ITXLivePlayListener {
            override fun onPlayEvent(p0: Int, p1: Bundle?) {
                if (p0 == TXLiveConstants.PLAY_EVT_PLAY_END ) {
                    Log.d(TAG, "playByTXPlayer complete")
                    player.stopPlay(true)
                }
            }

            override fun onNetStatus(p0: Bundle?) {

            }
        })
    }

    private fun getMp3Duration(rawId: Int) : Int {
        try {
            val uri = Uri.parse("android.resource://" + getPackageName() + "/" + rawId);
            val mediaPlayer = MediaPlayer()
            mediaPlayer.setDataSource(this, uri)
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.prepare()
            return mediaPlayer.getDuration()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return 0
    }

    /**
     * 获取当前音频的音量
     */
    private fun getStreamCurrentVolume(context: Context, streamType: Int): Int {
        val mAudioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager?
        return mAudioManager?.getStreamVolume(streamType) ?: 0
    }

}