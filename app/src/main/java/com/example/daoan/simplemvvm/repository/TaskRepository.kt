package com.example.daoan.simplemvvm.repository

import androidx.annotation.WorkerThread
import com.example.daoan.simplemvvm.data.model.Task
import com.vicpin.krealmextensions.queryAllAsFlowable
import com.vicpin.krealmextensions.save
import io.reactivex.Flowable

interface TaskRepository {
    fun getAllTasks(): Flowable<List<Task>>
    suspend fun insert(task: Task)
}

class TaskRepositoryImpl : TaskRepository {
    @WorkerThread
    override suspend fun insert(task: Task) {
        task.save()
    }

    override fun getAllTasks(): Flowable<List<Task>> {
        return Task().queryAllAsFlowable()
    }
}