package com.example.fozcoa

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

import android.widget.MediaController
import com.example.fozcoa.models.Videos
import kotlinx.android.synthetic.main.activity_video_player.*
import android.R.attr.start
import android.media.MediaPlayer
import android.R.attr.start
import android.support.v4.media.session.MediaControllerCompat.setMediaController
import android.widget.VideoView

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

        val mc = MediaController(this)
        mc.setAnchorView(videoView1)
        mc.setMediaPlayer(videoView1)
        val video = Uri.parse(videoItem!!.url)
        videoView1.setMediaController(mc)
        videoView1.setVideoURI(video)
        videoView1.start()
    }

}
