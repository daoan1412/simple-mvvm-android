package com.example.daoan.simplemvvm.ui.tasklist

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.EditorInfo
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.daoan.simplemvvm.R
import com.example.daoan.simplemvvm.app.hideKeyboard
import com.example.daoan.simplemvvm.data.model.Task
import com.example.daoan.simplemvvm.viewmodel.TaskViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_task_list.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.concurrent.TimeUnit

class TaskListFragment : Fragment(), ItemUserActionsListener {
    private val taskViewModel: TaskViewModel by viewModel()
    private val recyclerAdapter = TaskRecyclerViewAdapter(arrayListOf(), this)
    lateinit var itemTouchHelper: ItemTouchHelper
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
                taskViewModel.insert(Task(title = query))
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


    override fun onOptionsItemSelected(item: MenuItem) =
        when (item.itemId) {
            R.id.action_favorite -> {
                Log.i("skt", "action_favorite")
                true
            }
            else -> super.onOptionsItemSelected(item)
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
            .map { tasks -> tasks as ArrayList<Task> }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { tasks ->
                if (tasks.size > recyclerAdapter.tasks.size) {
                    recyclerAdapter.setData(tasks)
                    recyclerAdapter.scrollToTop(userRecyclerView)
                } else {
                    recyclerAdapter.setData(tasks)
                }
            })

        disposable.add(
            checkedItem
                .debounce(300, TimeUnit.MILLISECONDS)
                .switchMap { task ->
                    Observable.fromCallable {
                        taskViewModel.update(listOf(task))
                    }
                }
                .subscribe()
        )
    }

    private fun setUpRecyclerView() {
        val layoutManager = LinearLayoutManager(context)
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
        itemTouchHelper.attachToRecyclerView(userRecyclerView)
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
        checkedItemSubject.onNext(task)
    }

    companion object {
        fun newInstance() = TaskListFragment()
    }
}
