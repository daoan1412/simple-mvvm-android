package com.example.daoan.simplemvvm

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.daoan.simplemvvm.name.adapter.NameRecyclerViewAdapter
import com.example.daoan.simplemvvm.name.data.Name
import com.example.daoan.simplemvvm.name.viewmodel.NameViewModel
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private val nameViewModel: NameViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        nameRecyclerView.layoutManager = LinearLayoutManager(this)
        nameRecyclerView.adapter = NameRecyclerViewAdapter(mutableListOf())

        nameViewModel.allNames.observe(this, Observer<List<Name>> { allNames: List<Name> ->
            nameRecyclerView.adapter = NameRecyclerViewAdapter(allNames)
        }
        )

        insertBtn.setOnClickListener {
            nameViewModel.insertName(Name(name = nameInput.text.toString()))
            closeKeyboard(currentFocus)
        }
    }

    private fun closeKeyboard(view: View?) {
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}
