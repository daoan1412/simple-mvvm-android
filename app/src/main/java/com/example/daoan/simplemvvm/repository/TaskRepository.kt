package com.example.daoan.simplemvvm.repository

import androidx.annotation.WorkerThread
import com.example.daoan.simplemvvm.data.model.Task
import com.vicpin.krealmextensions.*
import io.reactivex.Completable
import io.reactivex.Flowable

interface TaskRepository {
    fun getAllTasks(): Flowable<List<Task>>
    suspend fun update(tasks: List<Task>)
    fun insert(task: Task): Completable
    suspend fun delete(task: Task)
}

class TaskRepositoryImpl : TaskRepository {
    override fun insert(task: Task): Completable {
        return Completable.fromAction{
            task.save()
        }
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