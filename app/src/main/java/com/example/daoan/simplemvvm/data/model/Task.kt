package com.example.daoan.simplemvvm.data.model

import io.objectbox.annotation.Backlink
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToMany

@Entity
data class Task(
    @Id
    var id: Long = 0,
    var description: String = "",
    var order: Long = System.currentTimeMillis(),
    var isCompleted: Boolean = false
    ) : RecyclerViewItem {
    @Backlink(to = "task")
    lateinit var steps: ToMany<Step>
}