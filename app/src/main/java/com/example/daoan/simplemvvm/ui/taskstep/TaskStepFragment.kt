package com.example.daoan.simplemvvm.ui.taskstep

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.daoan.simplemvvm.R


class TaskStepFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_task_step, container, false)
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        (activity as AppCompatActivity)
            .supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24px)

//        (activity as AppCompatActivity)
//            .supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }



    override fun onDetach() {
        super.onDetach()
//        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }


    companion object {
        fun newInstance() = TaskStepFragment()
    }
}
