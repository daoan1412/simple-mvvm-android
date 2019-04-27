package com.example.daoan.simplemvvm.repository

import com.example.daoan.simplemvvm.data.model.Step
import com.example.daoan.simplemvvm.data.model.Task
import com.example.daoan.simplemvvm.data.model.Task_
import io.objectbox.BoxStore
import io.objectbox.kotlin.boxFor
import io.objectbox.rx.RxQuery
import io.reactivex.Observable

interface TaskRepository {
    fun getAllTasks(): Observable<List<Task>>
    fun getAllSteps(): Observable<List<Step>>
    fun getTaskById(id: Long): Task?
    suspend fun insertOrUpdate(tasks: List<Task>)
    suspend fun insertOrUpdate(task: Task)
    suspend fun remove(tasks: List<Task>)
}

class TaskRepositoryImpl(boxStore: BoxStore) : TaskRepository {
    private val taskBox = boxStore.boxFor<Task>()
    private val stepBox = boxStore.boxFor<Step>()

    override fun getAllTasks(): Observable<List<Task>> {
        val query = taskBox.query().order(Task_.order).build()
        return RxQuery.observable(query)
    }

    override fun getAllSteps(): Observable<List<Step>> {
        val query = stepBox.query().build()
        return RxQuery.observable(query)
    }

    override fun getTaskById(id: Long): Task? {
        val query = taskBox.query().equal(Task_.id, id).build()
        return query.findFirst()
    }

    override suspend fun insertOrUpdate(tasks: List<Task>) {
        taskBox.put(tasks)
    }

    override suspend fun insertOrUpdate(task: Task) {
        taskBox.put(task)
        stepBox.put(task.steps)
    }

    override suspend fun remove(tasks: List<Task>) {
        taskBox.remove(tasks)
    }

}