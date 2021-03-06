package com.example.daoan.simplemvvm.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.daoan.simplemvvm.data.model.Task
import com.example.daoan.simplemvvm.repository.TaskRepository
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TaskViewModel(private val repo: TaskRepository) : ViewModel() {

    private val disposable = CompositeDisposable()
    private var _tasks = MutableLiveData<ArrayList<Task>>()
    val tasks: LiveData<ArrayList<Task>>
        get() = _tasks

    private val _selectedTask = MutableLiveData<Task>()
    val selectedTask: LiveData<Task>
        get() = _selectedTask

    init {
        getAllTasks()
    }

    private fun getAllTasks() {
        disposable.add(repo.getAllTasks()
            .subscribeOn(Schedulers.io())
            .map { tasks -> tasks as ArrayList<Task> }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { tasks ->
                _tasks.value = tasks
            }
        )
    }

    fun getTaskById(id: Long) {
        disposable.add(
            repo.getAllSteps().subscribeOn(Schedulers.io())
                .flatMap {
                    Observable.fromCallable {
                        repo.getTaskById(id)
                    }
                }
                .observeOn(Schedulers.computation())
                .map { task ->
                    task.steps.sortBy { step -> step.order }
                    return@map task
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { task ->
                    _selectedTask.value = task
                }
        )
    }

    fun insertOrUpdate(tasks: List<Task>) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.insertOrUpdate(tasks)
        }
    }

    fun insertOrUpdate(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.insertOrUpdate(task)
        }
    }

    fun remove(tasks: List<Task>) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.remove(tasks)
        }
    }


    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}