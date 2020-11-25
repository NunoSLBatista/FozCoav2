package com.fozcoa.fozcoa.adapters

import android.content.Context
import android.net.Uri
import android.opengl.Visibility
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.VideoView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fozcoa.fozcoa.R
import com.fozcoa.fozcoa.models.ImageGallery
import java.net.URI


class GalleryListAdapter (private val context: Context, private val imagesList: ArrayList<ImageGallery>, val listener : OnActionListener) : RecyclerView.Adapter<GalleryListAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryListAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.gallery_item, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
       return imagesList.size
    }

    override fun onBindViewHolder(holder: GalleryListAdapter.ViewHolder, position: Int) {

        val typeImage = imagesList.get(position).type

        if(typeImage == "video"){
            holder.imageView.isVisible = false
            holder.videoView.isVisible = true

            holder.videoView.setVideoURI(Uri.parse(imagesList.get(position).url));
            holder.videoView.seekTo( 1 );

        }else {
            holder.videoView.isVisible = false
            holder.imageView.isVisible = true
            Glide
                .with(context)
                .load(imagesList[position].url)
                .centerCrop()
                .placeholder(R.drawable.bg_miradouro_1)
                .into(holder.imageView);

            holder.imageView.setOnClickListener {
                listener.startActivity(context, imagesList.get(position))
            }
        }

    }

    class ViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {

        val imageView = itemView.findViewById(R.id.galleryItem) as ImageView
        val videoView = itemView.findViewById(R.id.videoItem) as VideoView

    }


    interface OnActionListener {
        fun startActivity(context: Context, galleryItem: ImageGallery)
    }

}