package com.example.daoan.simplemvvm.ui.main

import androidx.recyclerview.widget.RecyclerView
import com.example.daoan.simplemvvm.data.model.Task

interface ItemUserActionsListener {
    fun onItemDrag(viewHolder: RecyclerView.ViewHolder)
    fun onItemSwipe(task: Task)
    fun onItemReorder(tasks: List<Task>)
    fun onItemChecked(task: Task)
}
