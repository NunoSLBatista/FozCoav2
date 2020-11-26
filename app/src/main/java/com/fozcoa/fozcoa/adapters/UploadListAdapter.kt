package com.fozcoa.fozcoa.adapters

import android.content.Context
import android.graphics.Bitmap
import android.media.ThumbnailUtils
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.fozcoa.fozcoa.R
import android.util.Log
import android.graphics.BitmapFactory


class UploadListAdapter (private val context: Context, private val imagesList: ArrayList<Bitmap>, private val videoList: ArrayList<String>, val listener : OnActionListener) : RecyclerView.Adapter<UploadListAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UploadListAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.upload_item, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
       return imagesList.size + videoList.size
    }

    override fun onBindViewHolder(holder: UploadListAdapter.ViewHolder, position: Int) {
        //Images
        Log.d("Adapter", position.toString())
        if(position < imagesList.count()) {
            Log.d("Adapter", "Image")
            holder.imageView.setImageBitmap(imagesList.get(position))

            holder.imageView.setOnClickListener {
                listener.startActivity(context, imagesList.get(position))
            }
        }
        else{ //Videos
            val videoIndex = position - imagesList.count();

            Log.d("Adapter", "Video: " + videoList.get(videoIndex))
            val thumb = ThumbnailUtils.createVideoThumbnail(videoList.get(videoIndex), MediaStore.Video.Thumbnails.MICRO_KIND);
            holder.imageView.setImageBitmap(thumb)
        }
    }

    class ViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {

        val imageView = itemView.findViewById(R.id.galleryItem) as ImageView

    }


    interface OnActionListener {
        fun startActivity(context: Context, bitmap: Bitmap)
    }

}