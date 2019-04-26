package com.example.daoan.simplemvvm.ui.taskstep

import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.daoan.simplemvvm.R
import com.example.daoan.simplemvvm.base.inflate
import com.example.daoan.simplemvvm.data.model.Step
import com.example.daoan.simplemvvm.ui.common.ItemSelectedListener
import com.example.daoan.simplemvvm.ui.common.ItemTouchHelperListener
import com.example.daoan.simplemvvm.ui.common.ItemUserActionsListener
import io.realm.RealmList
import kotlinx.android.synthetic.main.name_item_view_holder.view.*
import java.util.*

class StepRecyclerViewAdapter(
    val steps: RealmList<Step>,
    private val itemUserActionListener: ItemUserActionsListener
) :
    RecyclerView.Adapter<StepRecyclerViewAdapter.ViewHolder>(),
    ItemTouchHelperListener {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent.inflate(R.layout.name_item_view_holder))
    }

    override fun getItemCount() = steps.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val steps = steps[position]
        holder.bind(steps!!)
    }

    fun setData(steps: RealmList<Step>) {
        this.steps.clear()
        this.steps.addAll(steps)
        notifyDataSetChanged()
    }

    fun scrollToTop(recyclerView: RecyclerView) {
        if (steps.size > 2) {
            recyclerView.scrollToPosition(steps.size - 1)
        }
    }

    override fun onItemMove(recyclerView: RecyclerView, fromPosition: Int, toPosition: Int): Boolean {
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                reOrderTask(steps, i, i + 1)
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                reOrderTask(steps, i, i - 1)
            }
        }
        notifyItemMoved(fromPosition, toPosition)
        return true
    }

    private fun reOrderTask(steps: RealmList<Step>, id1: Int, id2: Int) {
        Collections.swap(steps, id1, id2)
        val temp = steps[id1]!!.order
        steps[id1]!!.order = steps[id2]!!.order
        steps[id2]!!.order = temp
    }

    override fun onItemDismiss(viewHolder: RecyclerView.ViewHolder, position: Int) {
        itemUserActionListener.onItemSwipe(steps[position]!!)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        ItemSelectedListener {
        override fun onItemCleared() {
            itemUserActionListener.onItemReorder(steps)
        }

        fun bind(step: Step) {
            itemView.complete.isChecked = step.isCompleted
            itemView.nameItem.text = step.title
            itemView.handle.setOnTouchListener { v, event ->
                if (event.action == MotionEvent.ACTION_DOWN) {
                    itemUserActionListener.onItemDrag(this)
                }
                false
            }
            itemView.complete.setOnClickListener {
                step.isCompleted = !step.isCompleted
                itemUserActionListener.onItemChecked(step)
            }
        }
    }
}