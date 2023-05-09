package com.subhajit0061.notes.functions

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.subhajit0061.notes.Home
import com.subhajit0061.notes.R

class MyAdapter(private val context: Context) : Adapter<MyAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_list, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return Home.list.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    class MyViewHolder(itemView: View) : ViewHolder(itemView) {
        val txt_title: TextView = itemView.findViewById(R.id.txt_title)
        val txt_date: TextView = itemView.findViewById(R.id.txt_date)
        val btn_delete: ImageView = itemView.findViewById(R.id.btn_delete)
        val item: RelativeLayout = itemView.findViewById(R.id.item)
    }
}