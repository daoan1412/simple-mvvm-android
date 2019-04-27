package com.example.daoan.simplemvvm.data.model

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToOne

@Entity
data class Step(
    @Id
    var id: Long = 0,
    var description: String = "",
    var order: Long = System.currentTimeMillis(),
    var isCompleted: Boolean = false
) : RecyclerViewItem {
    lateinit var task: ToOne<Task>
}