package com.example.fozcoa.adapters

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.fozcoa.R
import com.example.fozcoa.models.ImageGallery
import com.example.fozcoa.models.Miradouro
import com.example.fozcoa.models.TypeGalleryItem
import com.squareup.picasso.Picasso
import android.text.Html


class TypeGalleryListAdapter (private val context: Context, private val typesList: ArrayList<TypeGalleryItem>, val positionSelected: Int, val listener : OnActionListener) : RecyclerView.Adapter<TypeGalleryListAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TypeGalleryListAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.type_gallery_item, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
       return typesList.size
    }

    override fun onBindViewHolder(holder: TypeGalleryListAdapter.ViewHolder, position: Int) {

        val normalFont : Typeface? = ResourcesCompat.getFont(context, R.font.montserrat)
        val semiBoldFont : Typeface? = ResourcesCompat.getFont(context, R.font.montserrat_semi_bold)

        if(positionSelected == position){
            holder.typeTextView.text = typesList.get(position).name
            holder.typeTextView.typeface = semiBoldFont
        } else {
            holder.typeTextView.text = typesList.get(position).name
            holder.typeTextView.typeface = normalFont
        }

        holder.typeTextView.setOnClickListener {
            listener.startActivity(context, typesList.get(position), position)
        }

    }

    class ViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {
        val typeTextView = itemView.findViewById(R.id.typeTextView) as TextView
    }


    interface OnActionListener {
        fun startActivity(context: Context, typeItem: TypeGalleryItem, position: Int)
    }

}