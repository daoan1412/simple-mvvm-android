package com.example.daoan.simplemvvm.data.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

open class Task(
    @PrimaryKey var id: String = UUID.randomUUID().toString(),
    var title: String,
    var isCompleted: Boolean = false
) : RealmObject() {
    constructor() : this("", "", false)
}

