package com.fozcoa.fozcoa.adapters

import android.content.Context
import android.graphics.Bitmap
import android.media.ThumbnailUtils
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.fozcoa.fozcoa.R


class ScrollGalleryAdapter (private val context: Context, val listener : OnActionListener) : RecyclerView.Adapter<ScrollGalleryAdapter.ViewHolder>(){

    private var imagesList: ArrayList<Bitmap> = ArrayList()
    private var videoList: ArrayList<String> = ArrayList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScrollGalleryAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.scroll_gallery_item, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
       return imagesList.size + videoList.size
    }

    override fun onBindViewHolder(holder: ScrollGalleryAdapter.ViewHolder, position: Int) {

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

        val imageView = itemView.findViewById(R.id.scrollImageView) as ImageView

    }

    fun setList(newList : ArrayList<Bitmap>, videoList : ArrayList<String>){
        imagesList = newList
        this.videoList = videoList
        notifyDataSetChanged()
    }


    interface OnActionListener {
        fun startActivity(context: Context, bitmap: Bitmap)
    }

}