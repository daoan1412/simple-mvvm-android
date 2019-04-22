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
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers


class MainActivity : AppCompatActivity(), ItemUserActionsListener {

    private val taskViewModel: TaskViewModel by viewModel()
    private val recyclerAdapter = TaskRecyclerViewAdapter(arrayListOf(), this)
    lateinit var itemTouchHelper: ItemTouchHelper
    private val disposable = CompositeDisposable()

    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setUpRecyclerView()
        setUpInsert()
        setUpItemTouchHelper()
    }

    override fun onStart() {
        super.onStart()
        setUpObserver()
    }

    override fun onStop() {
        super.onStop()
        disposable.clear()
    }

    @SuppressLint("CheckResult")
    private fun setUpObserver() {
        disposable.add(taskViewModel.tasks
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { tasks ->
                recyclerAdapter.setData(tasks as ArrayList<Task>)
            })
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
            val text = userNameInput.text.toString()
            userNameInput.setText("")
            taskViewModel.insert(Task(title = text))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    recyclerAdapter.scrollToTop(userRecyclerView)
                }
            window.decorView.hideKeyboard()
            window.decorView.clearFocus()
        }
    }

    override fun onItemDrag(viewHolder: RecyclerView.ViewHolder) {
        itemTouchHelper.startDrag(viewHolder)
    }

    override fun onItemSwipe(task: Task) {
        taskViewModel.delete(task)
    }

    override fun onItemReorder(tasks: List<Task>) {
        taskViewModel.update(tasks)
    }

    override fun onItemChecked(task: Task) {
        taskViewModel.update(listOf(task))
    }
}
