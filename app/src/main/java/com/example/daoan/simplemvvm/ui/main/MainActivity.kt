package com.example.daoan.simplemvvm.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.daoan.simplemvvm.R
import com.example.daoan.simplemvvm.data.model.User
import com.example.daoan.simplemvvm.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.daoan.simplemvvm.app.hideKeyboard


class MainActivity : AppCompatActivity(), ItemDragListener {

    private val userViewModel: UserViewModel by viewModel()
    private val recyclerAdapter = UserRecyclerViewAdapter(arrayListOf(), this)
    lateinit var itemTouchHelper: ItemTouchHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        setUpRecyclerView()
        subscribeToNavigationChanges(userViewModel)
        setUpInsert()
        setUpItemTouchHelper()
    }

    private fun subscribeToNavigationChanges(userViewModel: UserViewModel) {
        userViewModel.allUsers.observe(this, Observer { allUsers ->
            val usernames = allUsers.map { user -> user.username } as ArrayList<String>
            recyclerAdapter.setData(usernames)
            if (usernames.size > 1) {
                userRecyclerView.scrollToPosition(usernames.size - 1)
            }
        }
        )
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
            userViewModel.insertName(User(username = userNameInput.text.toString()))
            userNameInput.setText("")
            window.decorView.hideKeyboard()
            window.decorView.clearFocus()
        }
    }

    override fun onItemDrag(viewHolder: RecyclerView.ViewHolder) {
        itemTouchHelper.startDrag(viewHolder)
    }


}
