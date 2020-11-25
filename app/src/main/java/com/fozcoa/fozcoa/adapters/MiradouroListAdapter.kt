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
import com.fozcoa.fozcoa.models.Miradouro


class MiradouroListAdapter (private val context: Context, private val miradouroList: ArrayList<Miradouro>, val listener : OnActionListener) : RecyclerView.Adapter<MiradouroListAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MiradouroListAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.miradouro_item, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
       return miradouroList.size
    }

    override fun onBindViewHolder(holder: MiradouroListAdapter.ViewHolder, position: Int) {

        holder.titleTextView.text = miradouroList[position].name
       // Picasso.with(context).load(miradouroList[position].mainPicture).into(holder.imageView)
        Glide
            .with(context)
            .load(miradouroList[position].mainPicture)
            .centerCrop()
            .placeholder(R.drawable.bg_miradouro_1)
            .into(holder.imageView);
        holder.carView.setOnClickListener {
            listener.startActivity(context, miradouroList.get(position))
        }

    }

    class ViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {

        val titleTextView = itemView.findViewById(R.id.titleMiradouro) as TextView
        val imageView = itemView.findViewById(R.id.myImageView) as ImageView
        val carView = itemView.findViewById(R.id.boxMiradouro) as CardView

    }


    interface OnActionListener {
        fun startActivity(context: Context, miradouro: Miradouro)
    }

}