package com.example.daoan.simplemvvm.ui.main

import androidx.recyclerview.widget.RecyclerView

interface ItemUserActionsListener {
    fun onItemDrag(viewHolder: RecyclerView.ViewHolder)
    fun onItemSwipe(uid: Int)
}
