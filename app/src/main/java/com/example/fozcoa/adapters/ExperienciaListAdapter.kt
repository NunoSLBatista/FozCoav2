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
import com.example.fozcoa.models.Experiencia
import com.example.fozcoa.models.Miradouro
import com.squareup.picasso.Picasso


class ExperienciaListAdapter (private val context: Context, private val experienciaList: ArrayList<Experiencia>, val listener : OnActionListener) : RecyclerView.Adapter<ExperienciaListAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExperienciaListAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.experiencia_item, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
       return experienciaList.size
    }

    override fun onBindViewHolder(holder: ExperienciaListAdapter.ViewHolder, position: Int) {

        holder.titleTextView.text = experienciaList[position].name
        Picasso.with(context).load(experienciaList[position].mainImage).into(holder.imageView)

        holder.carView.setOnClickListener {
            listener.startActivity(context, experienciaList.get(position))
        }

    }

    class ViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {

        val titleTextView = itemView.findViewById(R.id.titleExperiencia) as TextView
        val imageView = itemView.findViewById(R.id.myImageView) as ImageView
        val carView = itemView.findViewById(R.id.boxExperiencia) as CardView

    }


    interface OnActionListener {
        fun startActivity(context: Context, experiencia: Experiencia)
    }

}