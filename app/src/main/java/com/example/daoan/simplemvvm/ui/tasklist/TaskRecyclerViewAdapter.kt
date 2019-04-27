package com.example.daoan.simplemvvm.ui.tasklist

import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.daoan.simplemvvm.R
import com.example.daoan.simplemvvm.base.inflate
import com.example.daoan.simplemvvm.data.model.Task
import com.example.daoan.simplemvvm.ui.common.ItemSelectedListener
import com.example.daoan.simplemvvm.ui.common.ItemTouchHelperListener
import com.example.daoan.simplemvvm.ui.common.ItemUserActionsListener
import kotlinx.android.synthetic.main.name_item_view_holder.view.*
import java.util.*

class TaskRecyclerViewAdapter(
    val tasks: ArrayList<Task>,
    private val itemUserActionListener: ItemUserActionsListener
) :
    RecyclerView.Adapter<TaskRecyclerViewAdapter.ViewHolder>(),
    ItemTouchHelperListener {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent.inflate(R.layout.name_item_view_holder))
    }

    override fun getItemCount() = tasks.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val task = tasks[position]
        holder.bind(task)
    }

    fun setData(tasks: ArrayList<Task>, recyclerView: RecyclerView) {
        val itemInserted = tasks.size > this.tasks.size
        this.tasks.clear()
        this.tasks.addAll(tasks)
        notifyDataSetChanged()
        if (itemInserted) {
            recyclerView.scrollToPosition(tasks.size - 1)
        }
    }

    override fun onItemMove(recyclerView: RecyclerView, fromPosition: Int, toPosition: Int): Boolean {
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                reOrderTask(tasks, i, i + 1)
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                reOrderTask(tasks, i, i - 1)
            }
        }
        notifyItemMoved(fromPosition, toPosition)
        return true
    }

    private fun reOrderTask(tasks: ArrayList<Task>, id1: Int, id2: Int) {
        Collections.swap(tasks, id1, id2)
        val temp = tasks[id1].order
        tasks[id1].order = tasks[id2].order
        tasks[id2].order = temp
    }

    override fun onItemDismiss(viewHolder: RecyclerView.ViewHolder, position: Int) {
        itemUserActionListener.onItemSwipe(tasks[position])
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        ItemSelectedListener {
        override fun onItemCleared() {
            itemUserActionListener.onItemReorder(tasks)
        }

        fun bind(task: Task) {
            itemView.complete.isChecked = task.isCompleted
            itemView.nameItem.text = task.description
            itemView.setOnClickListener {
                itemUserActionListener.onShowDetail(task)
            }
            itemView.handle.setOnTouchListener { v, event ->
                if (event.action == MotionEvent.ACTION_DOWN) {
                    itemUserActionListener.onItemDrag(this)
                }
                false
            }
            itemView.complete.setOnClickListener {
                task.isCompleted = !task.isCompleted
                itemUserActionListener.onItemChecked(task)
            }
        }
    }
}