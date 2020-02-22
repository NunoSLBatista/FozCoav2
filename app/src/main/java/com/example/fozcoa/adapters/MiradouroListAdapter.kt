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

class MiradouroListAdapter (private val context: Context, private val miradouroList: ArrayList<Miradouro>, val listener : MiradouroListAdapter.OnActionListener) : BaseAdapter(){

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
       var convertView: View? = convertView
        val holder: ViewHolder

        if(convertView == null){
            holder = ViewHolder()
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = inflater.inflate(R.layout.miradouro_item, null, true)

            holder.title = convertView!!.findViewById(R.id.)

        } else {
            holder = convertView.tag as ViewHolder
        }

        holder.title!!.text = miradouroList.get(position).



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
        var lineLayout: LinearLayout? = null
    }

    interface OnActionListener {
        fun startActivity(context: Context, miradouro: Miradouro)
    }

}