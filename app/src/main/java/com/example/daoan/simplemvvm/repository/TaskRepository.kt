package com.example.daoan.simplemvvm.repository

import androidx.annotation.WorkerThread
import com.example.daoan.simplemvvm.data.model.Task
import com.vicpin.krealmextensions.delete
import com.vicpin.krealmextensions.queryAsFlowable
import com.vicpin.krealmextensions.save
import com.vicpin.krealmextensions.saveAll
import io.reactivex.Flowable

interface TaskRepository {
    fun getAllTasks(): Flowable<List<Task>>
    fun getTaskById(id: String): Flowable<Task>
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

    @WorkerThread
    override fun getTaskById(id: String): Flowable<Task> {
        return Task().queryAsFlowable {
            equalTo("id", id)
        }.filter { tasks -> tasks.size == 1 }
            .map { tasks -> tasks[0] }
    }

}