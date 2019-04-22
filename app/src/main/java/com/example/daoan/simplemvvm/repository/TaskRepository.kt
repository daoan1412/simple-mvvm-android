package com.example.daoan.simplemvvm.repository

import androidx.annotation.WorkerThread
import com.example.daoan.simplemvvm.data.model.Task
import com.vicpin.krealmextensions.*
import io.reactivex.Flowable

interface TaskRepository {
    fun getAllTasks(): Flowable<List<Task>>
    suspend fun update(tasks: List<Task>)
    suspend fun insert(task: Task)
    suspend fun delete(task: Task)
}

class TaskRepositoryImpl : TaskRepository {
    @WorkerThread
    override suspend fun insert(task: Task) {
        task.save()
    }

    @WorkerThread
    override suspend fun update(tasks: List<Task>) {
        tasks.saveAll()
    }

    @WorkerThread
    override suspend fun delete(task: Task) {
        Task().delete { equalTo("id", task.id) }
    }

    override fun getAllTasks(): Flowable<List<Task>> {
        return Task().queryAsFlowable {
            sort("order")
        }
    }

}