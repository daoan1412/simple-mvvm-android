package com.example.daoan.simplemvvm.ui.taskstep

import android.os.Bundle
import android.view.*
import android.view.inputmethod.EditorInfo
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.daoan.simplemvvm.R
import com.example.daoan.simplemvvm.base.hideKeyboard
import com.example.daoan.simplemvvm.data.model.RecyclerViewItem
import com.example.daoan.simplemvvm.data.model.Step
import com.example.daoan.simplemvvm.data.model.Task
import com.example.daoan.simplemvvm.ui.common.ItemTouchHeplerCallBack
import com.example.daoan.simplemvvm.ui.common.ItemUserActionsListener
import com.example.daoan.simplemvvm.viewmodel.TaskViewModel
import io.realm.RealmList
import kotlinx.android.synthetic.main.fragment_task_list.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class TaskStepFragment : Fragment(), ItemUserActionsListener {
    private val taskViewModel: TaskViewModel by viewModel()
    private val recyclerAdapter = StepRecyclerViewAdapter(RealmList(), this)
    private lateinit var itemTouchHelper: ItemTouchHelper
    lateinit var searchItem: MenuItem


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_task_step, container, false)
        setHasOptionsMenu(true)
        return rootView
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_action_bar, menu)
        searchItem = menu.findItem(R.id.action_a_task)
        val searchView = searchItem.actionView as SearchView
        searchView.imeOptions = EditorInfo.IME_ACTION_DONE
        searchView.maxWidth = Integer.MAX_VALUE
        searchView.queryHint = "Add a Step"
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
                val taskId = arguments?.getString("selectedId", "")
                taskViewModel.insertStep(taskId!!, Step(title = query))
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


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setUpRecyclerView()
        setUpItemTouchHelper()
        taskViewModel.getTaskById(
            arguments?.getString("selectedId")!!
        )
        taskViewModel.selectedTask.observe(this, Observer { task ->
            val steps = task.steps
            if (steps.size > recyclerAdapter.steps.size) {
                recyclerAdapter.setData(steps)
                recyclerAdapter.scrollToTop(userRecyclerView)
            } else {
                recyclerAdapter.setData(steps)
            }
        })
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

    override fun onItemSwipe(item: RecyclerViewItem) {
        taskViewModel.delete(arguments?.getString("selectedId")!!, item as Step)
    }

    override fun onItemReorder(items: Collection<RecyclerViewItem>) {
//        taskViewModel.update(arguments?.getString("selectedId")!!, items as RealmList<Step>)
    }

    override fun onShowDetail(task: Task) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onItemChecked(item: RecyclerViewItem) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    companion object {
        fun newInstance() = TaskStepFragment()
    }
}
