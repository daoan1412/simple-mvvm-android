package com.example.daoan.simplemvvm.name.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.daoan.simplemvvm.R
import com.example.daoan.simplemvvm.name.data.Name
import kotlinx.android.synthetic.main.name_item_view_holder.view.*

class NameRecyclerViewAdapter(val allNames: List<Name>) : RecyclerView.Adapter<NameRecyclerViewAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.name_item_view_holder, parent, false))
    }

    override fun getItemCount(): Int {
        return allNames.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val name = allNames[position]
        holder.bind(name)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(name: Name) {
            itemView.nameItem.text = name.name
        }
    }
}