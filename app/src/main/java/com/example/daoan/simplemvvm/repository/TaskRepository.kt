package com.example.daoan.simplemvvm.repository

import androidx.annotation.WorkerThread
import com.example.daoan.simplemvvm.data.model.Step
import com.example.daoan.simplemvvm.data.model.Task
import com.vicpin.krealmextensions.*
import io.reactivex.Flowable
import io.realm.RealmList

interface TaskRepository {
    fun getAllTasks(): Flowable<List<Task>>
    suspend fun update(tasks: List<Task>)
    suspend fun insert(task: Task)
    suspend fun delete(task: Task)
    fun getTaskById(id: String): Flowable<Task>
    fun insertStepByTaskId(taskId: String, step: Step)
    fun updateStepsByTaskId(taskId: String, steps: RealmList<Step>)
    fun delete(taskId: String, step: Step)
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

    private fun findTaskById(taskId: String): Task? {
        return Task().query {
            equalTo("id", taskId)
        }.firstOrNull()
    }

    override fun insertStepByTaskId(taskId: String, step: Step) {
        val task = findTaskById(taskId)

        task?.let {
            task.steps.add(step)
            task.save()
        }
    }

    override fun updateStepsByTaskId(taskId: String, steps: RealmList<Step>) {
        val task = findTaskById(taskId)

        task?.let {
            task.steps.clear()
            task.steps.addAll(steps)
            task.save()
        }
    }

    override fun delete(taskId: String, step: Step) {
        val task = findTaskById(taskId)

        task?.let {
            task.steps.remove(step)
            task.save()
        }
    }

}