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
import com.example.fozcoa.models.Miradouro
import com.example.fozcoa.models.Videos
import com.squareup.picasso.Picasso


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
        if(videosList[position].url != ""){
            Picasso.with(context).load(videosList[position].url).into(holder.imageView)
        }

        holder.carView.setOnClickListener {
            listener.startActivity(context, videosList.get(position))
        }

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