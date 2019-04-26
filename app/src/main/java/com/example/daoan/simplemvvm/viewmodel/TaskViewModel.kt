package com.example.daoan.simplemvvm.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.daoan.simplemvvm.data.model.Step
import com.example.daoan.simplemvvm.data.model.Task
import com.example.daoan.simplemvvm.repository.TaskRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.realm.RealmList
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

    fun getTaskById(id: String) {
        disposable.add(repo.getTaskById(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { selectedTask ->
                _selectedTask.value = selectedTask
            }
        )
    }

    fun insertStep(selectedId: String, step: Step) {
        repo.insertStepByTaskId(selectedId, step)
    }

    fun insert(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.insert(task)
        }
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

    fun delete(taskId: String, step: Step) {
        repo.delete(taskId, step)
    }

    fun update(taskId: String, steps: RealmList<Step>) {
        repo.updateStepsByTaskId(
            taskId, steps
        )
    }


    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}