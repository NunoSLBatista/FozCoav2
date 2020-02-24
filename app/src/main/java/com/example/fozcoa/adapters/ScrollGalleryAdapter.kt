package com.example.fozcoa.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.fozcoa.R
import com.example.fozcoa.models.ImageGallery
import com.example.fozcoa.models.Miradouro
import com.squareup.picasso.Picasso


class ScrollGalleryAdapter (private val context: Context, private val imagesList: ArrayList<ImageGallery>, val listener : OnActionListener) : RecyclerView.Adapter<ScrollGalleryAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScrollGalleryAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.scroll_gallery_item, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
       return imagesList.size
    }

    override fun onBindViewHolder(holder: ScrollGalleryAdapter.ViewHolder, position: Int) {


        Picasso.with(context).load(imagesList[position].url).into(holder.imageView)

        holder.imageView.setOnClickListener {
            listener.startActivity(context, imagesList.get(position))
        }

    }

    class ViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {

        val imageView = itemView.findViewById(R.id.scrollImageView) as ImageView

    }


    interface OnActionListener {
        fun startActivity(context: Context, galleryItem: ImageGallery)
    }

}