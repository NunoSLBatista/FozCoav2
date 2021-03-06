package com.fozcoa.fozcoa.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fozcoa.fozcoa.R
import com.fozcoa.fozcoa.models.Videos
import android.media.MediaMetadataRetriever
import android.graphics.Bitmap
import android.R.attr.bitmap
import android.os.Build
import android.util.Log
import android.widget.VideoView
import androidx.core.content.ContextCompat
import com.bumptech.glide.request.RequestOptions
import com.google.vr.cardboard.ThreadUtils


class VideosListAdapter (private val context: Context, private val videosList: ArrayList<Videos>, val listener : OnActionListener) : RecyclerView.Adapter<VideosListAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideosListAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.video_item, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return videosList.size
    }

    override fun onBindViewHolder(holder: VideosListAdapter.ViewHolder, position: Int) {

        holder.titleTextView.text = videosList[position].name

        holder.carView.setOnClickListener {
            listener.startActivity(context, videosList.get(position))
        }
        holder.imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.no_image))
        val thread = Thread(Runnable {
            var bitmap =
                retriveVideoFrameFromVideo(videosList.get(position).uri.toString())
            if (bitmap != null) {
                ThreadUtils.runOnUiThread(Thread(Runnable {
                    holder.imageView.setImageBitmap(bitmap)
                }))
            }
        })
        thread.start()
    }

    fun retriveVideoFrameFromVideo(videoPath: String): Bitmap? {
        var bitmap: Bitmap? = null
        var mediaMetadataRetriever: MediaMetadataRetriever? = null
        try {
            mediaMetadataRetriever = MediaMetadataRetriever()
            mediaMetadataRetriever.setDataSource(videoPath, HashMap())
            //   mediaMetadataRetriever.setDataSource(videoPath);
            bitmap = mediaMetadataRetriever.frameAtTime
        } catch (e: Exception) {
            return null;
        } finally {
            mediaMetadataRetriever?.release()
        }
        return bitmap
    }


    class ViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {

        val titleTextView = itemView.findViewById(R.id.titleVideo) as TextView
        val imageView = itemView.findViewById(R.id.myImageView) as ImageView
        val carView = itemView.findViewById(R.id.boxVideo) as CardView

    }


    interface OnActionListener {
        fun startActivity(context: Context, videos: Videos)
    }

}