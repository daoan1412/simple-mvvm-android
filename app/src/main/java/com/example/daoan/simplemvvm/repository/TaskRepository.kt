package com.example.daoan.simplemvvm.repository

import com.example.daoan.simplemvvm.data.model.Step
import com.example.daoan.simplemvvm.data.model.Task
import com.example.daoan.simplemvvm.data.model.Task_
import io.objectbox.BoxStore
import io.objectbox.kotlin.boxFor
import io.objectbox.rx.RxQuery
import io.reactivex.Flowable
import io.reactivex.Observable

interface TaskRepository {
    fun getAll(): Observable<List<Task>>
    fun getById(id: Long): Flowable<Task>
    suspend fun insertOrUpdate(tasks: List<Task>)
    suspend fun remove(tasks: List<Task>)
    suspend fun update(steps: List<Step>)
}

class TaskRepositoryImpl(boxStore: BoxStore) : TaskRepository {
    private val taskBox = boxStore.boxFor<Task>()
    private val stepBox = boxStore.boxFor<Step>()

    override fun getAll(): Observable<List<Task>> {
        val query = taskBox.query().order(Task_.order).build()
        return RxQuery.observable(query)
    }

    override fun getById(id: Long): Flowable<Task> {
        val query = taskBox.query()
            .equal(Task_.id, id)
            .build()
        return RxQuery.flowableOneByOne(query)
    }

    override suspend fun insertOrUpdate(tasks: List<Task>) {
        taskBox.put(tasks)
    }

    override suspend fun update(steps: List<Step>) {
        stepBox.put(steps)
    }

    override suspend fun remove(tasks: List<Task>) {
        taskBox.remove(tasks)
    }

}