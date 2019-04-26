package com.example.daoan.simplemvvm.data.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

open class Step(
    @PrimaryKey var id: String = UUID.randomUUID().toString(),
    var title: String,
    var order: Long = System.currentTimeMillis(),
    var isCompleted: Boolean = false
) : RealmObject(), RecyclerViewItem {
    constructor() : this("", "", 0, false)
}
