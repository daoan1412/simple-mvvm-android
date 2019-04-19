package com.example.daoan.simplemvvm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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
        }
    }


}
