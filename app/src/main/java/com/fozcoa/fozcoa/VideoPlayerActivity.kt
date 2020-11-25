package com.fozcoa.fozcoa

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

import android.widget.MediaController
import com.fozcoa.fozcoa.models.Videos
import kotlinx.android.synthetic.main.activity_video_player.*
import java.lang.Exception

class VideoPlayerActivity : AppCompatActivity() {

    private var mediaController: MediaController? = null
    private var TAG = "VideoPlayer"
    private var videoItem : Videos ? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_player)

        videoItem = intent.getParcelableExtra("video")

        configureVideoView()

    }

    private fun configureVideoView() {
        try{
            videoView1.setVideoURI(videoItem!!.uri)
            videoView1.start()
            val mc = MediaController(this)
            mc.setAnchorView(videoView1)
            mc.setMediaPlayer(videoView1)
            videoView1.setMediaController(mc)
            videoView1.requestFocus();

        } catch (e : Exception) {
            Log.e("Error", e.message.toString());
            e.printStackTrace();
        }
    }

}
