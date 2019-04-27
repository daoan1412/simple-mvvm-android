package com.example.daoan.simplemvvm.ui.tasklist

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import android.view.inputmethod.EditorInfo
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.daoan.simplemvvm.R
import com.example.daoan.simplemvvm.base.hideKeyboard
import com.example.daoan.simplemvvm.data.model.RecyclerViewItem
import com.example.daoan.simplemvvm.data.model.Task
import com.example.daoan.simplemvvm.ui.common.ItemTouchHeplerCallBack
import com.example.daoan.simplemvvm.ui.common.ItemUserActionsListener
import com.example.daoan.simplemvvm.viewmodel.TaskViewModel
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_task_list.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.concurrent.TimeUnit

class TaskListFragment : Fragment(), ItemUserActionsListener {
    private val taskViewModel: TaskViewModel by viewModel()
    private val recyclerAdapter = TaskRecyclerViewAdapter(arrayListOf(), this)
    private lateinit var itemTouchHelper: ItemTouchHelper
    private val disposable = CompositeDisposable()
    private val checkedItemSubject = PublishSubject.create<Task>()
    private val checkedItem: Observable<Task>
        get() = checkedItemSubject
    lateinit var searchItem: MenuItem

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_task_list, container, false)
        setHasOptionsMenu(true)
        return rootView
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setUpRecyclerView()
        setUpItemTouchHelper()
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        taskViewModel.tasks.observe(this, Observer { tasks ->
            recyclerAdapter.setData(tasks, taskListRecyclerView)
        })
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_action_bar, menu)
        searchItem = menu.findItem(R.id.action_a_task)
        val searchView = searchItem.actionView as SearchView
        searchView.imeOptions = EditorInfo.IME_ACTION_DONE
        searchView.maxWidth = Integer.MAX_VALUE
        searchView.queryHint = "Add a Task"
        searchView.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewDetachedFromWindow(arg0: View) {
                setItemsVisibility(menu, searchItem, true)
            }

            override fun onViewAttachedToWindow(arg0: View) {
                setItemsVisibility(menu, searchItem, false)
            }
        })

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                searchItem.collapseActionView()
                taskViewModel.insertOrUpdate(Task(description = query))
                return false
            }
        })
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun setItemsVisibility(menu: Menu, exception: MenuItem, visible: Boolean) {
        for (i in 0 until menu.size()) {
            val item = menu.getItem(i)
            if (item !== exception) item.isVisible = visible
        }
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
        disposable.add(
            checkedItem
                .debounce(300, TimeUnit.MILLISECONDS)
                .switchMap { task ->
                    Observable.fromCallable {
                        taskViewModel.insertOrUpdate(task)
                    }
                }
                .subscribe()
        )
    }

    private fun setUpRecyclerView() {
        val layoutManager = LinearLayoutManager(context)
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true
        taskListRecyclerView.layoutManager = layoutManager
        val dividerItemDecoration = DividerItemDecoration(
            taskListRecyclerView.context,
            layoutManager.orientation
        )
        taskListRecyclerView.addItemDecoration(dividerItemDecoration)
        taskListRecyclerView.adapter = recyclerAdapter

        taskListRecyclerView.setOnTouchListener { v, _ ->
            searchItem.collapseActionView()
            v.hideKeyboard()
            false
        }
    }

    private fun setUpItemTouchHelper() {
        itemTouchHelper = ItemTouchHelper(
            ItemTouchHeplerCallBack(
                recyclerAdapter
            )
        )
        itemTouchHelper.attachToRecyclerView(taskListRecyclerView)
    }

    override fun onItemDrag(viewHolder: RecyclerView.ViewHolder) {
        itemTouchHelper.startDrag(viewHolder)
    }

    override fun onItemSwipe(item: RecyclerViewItem) {
        taskViewModel.remove(listOf(item as Task))
    }

    override fun onItemReorder(items: Collection<RecyclerViewItem>) {
        taskViewModel.insertOrUpdate(items as List<Task>)
    }

    override fun onItemChecked(item: RecyclerViewItem) {
        checkedItemSubject.onNext(item as Task)
    }

    override fun onShowDetail(task: Task) {
        val bundle = bundleOf("selectedId" to task.id)
        findNavController().navigate(R.id.action_taskListFragment_to_taskStepFragment, bundle)
    }

    companion object {
        fun newInstance() = TaskListFragment()
    }
}
