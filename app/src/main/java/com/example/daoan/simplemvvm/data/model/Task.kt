package com.example.daoan.simplemvvm.data.model

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

open class Task(
    @PrimaryKey var id: String = UUID.randomUUID().toString(),
    var title: String,
    var order: Long = System.currentTimeMillis(),
    var isCompleted: Boolean = false,
    var steps: RealmList<Step> = RealmList()
) : RealmObject(), RecyclerViewItem {
    constructor() : this("", "", 0, false, RealmList())
}

