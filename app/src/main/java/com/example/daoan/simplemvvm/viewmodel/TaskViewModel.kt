package com.example.daoan.simplemvvm.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.daoan.simplemvvm.data.model.Task
import com.example.daoan.simplemvvm.repository.TaskRepository
import io.reactivex.Completable
import io.reactivex.Flowable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TaskViewModel(private val repo: TaskRepository) : ViewModel() {

    val tasks: Flowable<List<Task>> = repo.getAllTasks()

    fun insert(task: Task): Completable {
        return repo.insert(task)
    }

    fun update(tasks: List<Task>) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.update(tasks)
        }
    }

    fun delete(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.delete(task)
        }
    }

}