package com.example.fozcoa.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fozcoa.R
import com.example.fozcoa.models.Miradouro
import com.example.fozcoa.models.Videos

class VideosListAdapter (private val context: Context, private val videosList: ArrayList<Videos>, val listener : OnActionListener) : BaseAdapter(){

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
       var convertView: View? = convertView
        val holder: ViewHolder

        if(convertView == null){
            holder = ViewHolder()
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = inflater.inflate(R.layout.miradouro_item, null, true)

            holder.title = convertView!!.findViewById(R.id.titleMiradouro)
            holder.linearLayout = convertView.findViewById(R.id.miradouroLayout)

        } else {
            holder = convertView.tag as ViewHolder
        }

        holder.title!!.text = videosList.get(position).name


        holder.linearLayout!!.setOnClickListener {
            listener.startActivity(context, videosList[position])
        }

        return convertView

    }

    override fun getItem(position: Int): Any {
       return videosList[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getCount(): Int {
       return videosList.size
    }

    private inner class ViewHolder {
        var title: TextView? = null
        var linearLayout: LinearLayout? = null
    }

    interface OnActionListener {
        fun startActivity(context: Context, video: Videos)
    }

}