package com.example.fozcoa.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.fozcoa.R
import com.example.fozcoa.models.Miradouro
import com.squareup.picasso.Picasso

class MiradouroListAdapter (private val context: Context, private val miradouroList: ArrayList<Miradouro>, val listener : OnActionListener) : BaseAdapter(){

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var convertView: View? = convertView
        val holder: ViewHolder

        if(convertView == null){
            holder = ViewHolder()
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = inflater.inflate(R.layout.miradouro_item, null, true)

            holder.title = convertView!!.findViewById(R.id.titleMiradouro)
            holder.imageView = convertView.findViewById(R.id.myImageView)
            holder.cardView = convertView.findViewById(R.id.boxMiradouro)

            convertView.tag = holder

        } else {
            holder = convertView.tag as ViewHolder
        }

        holder.title!!.text = miradouroList.get(position).name
        Picasso.with(context).load(miradouroList.get(position).mainPicture).into(holder.imageView)

        holder.cardView!!.setOnClickListener {
            listener.startActivity(context, miradouroList.get(position))
        }


        return convertView

    }

    override fun getItem(position: Int): Any {
       return miradouroList[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getCount(): Int {
       return miradouroList.size
    }

    private inner class ViewHolder {
        var title: TextView? = null
        var imageView: ImageView? = null
        var cardView: CardView? = null
    }

    interface OnActionListener {
        fun startActivity(context: Context, miradouro: Miradouro)
    }

}