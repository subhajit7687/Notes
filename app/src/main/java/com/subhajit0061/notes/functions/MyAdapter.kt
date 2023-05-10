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

class MyAdapter(private val context: Context, val onItemClick: OnItemClick) : Adapter<MyAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_list, parent, false)
        return MyViewHolder(view,onItemClick)
    }

    override fun getItemCount(): Int {
        return Home.list.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.txt_title.text = Home.list[position].tile
        holder.txt_date.text = Home.list[position].date
    }

    class MyViewHolder(itemView: View,onItemClick: OnItemClick) : ViewHolder(itemView) {
        val txt_title: TextView = itemView.findViewById(R.id.txt_title)
        val txt_date: TextView = itemView.findViewById(R.id.txt_date)
        val btn_delete: ImageView = itemView.findViewById(R.id.btn_delete)
        val item: RelativeLayout = itemView.findViewById(R.id.item)

        init{
            item.setOnClickListener {
                onItemClick.onItemClick(adapterPosition)
            }

            btn_delete.setOnClickListener {
                onItemClick.onDeleteClick(adapterPosition)
            }
        }
    }
}