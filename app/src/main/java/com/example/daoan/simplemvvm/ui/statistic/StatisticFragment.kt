package com.example.daoan.simplemvvm.ui.statistic

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.daoan.simplemvvm.R
import com.example.daoan.simplemvvm.viewmodel.TaskViewModel
import kotlinx.android.synthetic.main.fragment_statistic.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class StatisticFragment : Fragment() {
    private val taskViewModel: TaskViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_statistic, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        show.setOnClickListener {
            Log.i("skt", "Hello")
            taskViewModel.getTaskById("fdd7d91f-e839-47d6-95a9-a88ade1dae1a")
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        taskViewModel.selectedTask.observe(this, Observer {
            task -> Log.i("skt", task.id)
        })
    }

    companion object {
        fun newInstance() = StatisticFragment()
    }
}
