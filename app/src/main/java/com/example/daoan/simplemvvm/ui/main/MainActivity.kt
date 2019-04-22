package com.example.daoan.simplemvvm.ui.main

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.daoan.simplemvvm.R
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.daoan.simplemvvm.app.hideKeyboard
import com.example.daoan.simplemvvm.data.model.Task
import com.example.daoan.simplemvvm.viewmodel.TaskViewModel
import io.reactivex.android.schedulers.AndroidSchedulers


class MainActivity : AppCompatActivity(), ItemUserActionsListener {

    private val taskViewModel: TaskViewModel by viewModel()
    private val recyclerAdapter = TaskRecyclerViewAdapter(arrayListOf(), this)
    lateinit var itemTouchHelper: ItemTouchHelper

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setUpRecyclerView()
        setUpInsert()
        setUpItemTouchHelper()
        taskViewModel.tasks.observeOn(AndroidSchedulers.mainThread())
            .subscribe {
            tasks ->
                recyclerAdapter.setData(tasks as ArrayList<Task>)
                if (tasks.size > 1) {
                    userRecyclerView.scrollToPosition(tasks.size - 1)
                }
                Log.d("skt", tasks.map { task -> task.title }.toString())

        }
    }

    private fun setUpRecyclerView() {
        val layoutManager = LinearLayoutManager(this)
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true
        userRecyclerView.layoutManager = layoutManager
        val dividerItemDecoration = DividerItemDecoration(
            userRecyclerView.context,
            layoutManager.orientation
        )
        userRecyclerView.addItemDecoration(dividerItemDecoration)
        userRecyclerView.adapter = recyclerAdapter

        userRecyclerView.setOnTouchListener { v, _ ->
            v.hideKeyboard()
            false
        }
    }

    private fun setUpItemTouchHelper() {
        itemTouchHelper = ItemTouchHelper(ItemTouchHeplerCallBack(recyclerAdapter))
        itemTouchHelper.attachToRecyclerView(userRecyclerView)
    }

    private fun setUpInsert() {
        insertBtn.setOnClickListener {
            val text =  userNameInput.text.toString()
            taskViewModel.insert(Task(title = text))
            userNameInput.setText("")
            window.decorView.hideKeyboard()
            window.decorView.clearFocus()
        }
    }

    override fun onItemDrag(viewHolder: RecyclerView.ViewHolder) {
        itemTouchHelper.startDrag(viewHolder)
    }

    override fun onItemSwipe(uid: Int) {
    }
}
