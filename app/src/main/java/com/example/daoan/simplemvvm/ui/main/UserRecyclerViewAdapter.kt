package com.example.daoan.simplemvvm.ui.main

import android.view.ContextMenu
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.daoan.simplemvvm.R
import com.example.daoan.simplemvvm.app.inflate
import kotlinx.android.synthetic.main.name_item_view_holder.view.*
import java.util.*
import kotlin.collections.ArrayList

class UserRecyclerViewAdapter(
    val usernames: ArrayList<String>,
    private val itemDragListener: ItemDragListener
) :
    RecyclerView.Adapter<UserRecyclerViewAdapter.ViewHolder>(), ItemTouchHelperListener {
    override fun onItemDismiss(viewHolder: RecyclerView.ViewHolder, position: Int) {
        usernames.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent.inflate(R.layout.name_item_view_holder))
    }

    override fun getItemCount() = usernames.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val username = usernames[position]
        holder.bind(username)
    }

    fun setData(usernames: ArrayList<String>) {
        this.usernames.clear()
        this.usernames.addAll(usernames)
        notifyDataSetChanged()
    }

    override fun onItemMove(recyclerView: RecyclerView, fromPosition: Int, toPosition: Int): Boolean {
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                Collections.swap(usernames, i, i + 1)
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                Collections.swap(usernames, i, i - 1)
            }
        }
        notifyItemMoved(fromPosition, toPosition)
        return true
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), ItemSelectedListener {
        override fun onItemSelected() {
            itemView.listItemContainer.setBackgroundColor(
                ContextCompat.getColor(
                    itemView.context,
                    R.color.selectedItem
                )
            )
        }

        override fun onItemCleared() {
            itemView.listItemContainer.setBackgroundColor(0)
        }

        fun bind(username: String) {
            itemView.nameItem.text = username
            itemView.handle.setOnTouchListener { v, event ->
                if (event.action == MotionEvent.ACTION_DOWN) {
                    itemDragListener.onItemDrag(this)
                }
                false
            }
        }
    }
}