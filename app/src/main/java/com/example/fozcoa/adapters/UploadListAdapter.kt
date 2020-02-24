package com.example.fozcoa.adapters

import android.content.Context
import android.graphics.Bitmap
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


class UploadListAdapter (private val context: Context, private val imagesList: ArrayList<Bitmap>, val listener : OnActionListener) : RecyclerView.Adapter<UploadListAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UploadListAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.upload_item, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
       return imagesList.size
    }

    override fun onBindViewHolder(holder: UploadListAdapter.ViewHolder, position: Int) {

        holder.imageView.setImageBitmap(imagesList.get(position))

        holder.imageView.setOnClickListener {
            listener.startActivity(context, imagesList.get(position))
        }

    }

    class ViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {

        val imageView = itemView.findViewById(R.id.galleryItem) as ImageView

    }


    interface OnActionListener {
        fun startActivity(context: Context, bitmap: Bitmap)
    }

}