package com.example.daoan.simplemvvm.ui.main

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.daoan.simplemvvm.R
import com.example.daoan.simplemvvm.data.model.User
import com.example.daoan.simplemvvm.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private val userViewModel: UserViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        userRecyclerView.layoutManager = LinearLayoutManager(this)
        userRecyclerView.adapter = UserRecyclerViewAdapter(mutableListOf())

        userViewModel.allNames.observe(this, Observer<List<User>> { allUsers: List<User> ->
            userRecyclerView.adapter =
                UserRecyclerViewAdapter(allUsers.map { user ->
                    user.username
                })
        }
        )

        insertBtn.setOnClickListener {
            userViewModel.insertName(User(username = userNameInput.text.toString()))
            userNameInput.setText("")
            closeKeyboard(currentFocus)
            window.decorView.clearFocus()
        }
    }

    private fun closeKeyboard(view: View?) {
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}
