package com.example.daoan.simplemvvm.ui.common

import androidx.recyclerview.widget.RecyclerView
import com.example.daoan.simplemvvm.data.model.RecyclerViewItem
import com.example.daoan.simplemvvm.data.model.Task

interface ItemUserActionsListener {
    fun onItemDrag(viewHolder: RecyclerView.ViewHolder)
    fun onItemSwipe(item: RecyclerViewItem)
    fun onItemReorder(items: Collection<RecyclerViewItem>)
    fun onShowDetail(task: Task)
    fun onItemChecked(item: RecyclerViewItem)
}
