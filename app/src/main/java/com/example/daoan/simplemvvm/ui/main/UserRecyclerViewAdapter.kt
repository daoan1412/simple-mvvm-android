package com.example.daoan.simplemvvm.ui.main

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.daoan.simplemvvm.R
import com.example.daoan.simplemvvm.app.inflate
import kotlinx.android.synthetic.main.name_item_view_holder.view.*

class UserRecyclerViewAdapter(private val usernames: List<String>) : RecyclerView.Adapter<UserRecyclerViewAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent.inflate(R.layout.name_item_view_holder))
    }

    override fun getItemCount() = usernames.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val username = usernames[position]
        holder.bind(username)
    }

   inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(username: String) {
            itemView.nameItem.text = username
        }
    }
}