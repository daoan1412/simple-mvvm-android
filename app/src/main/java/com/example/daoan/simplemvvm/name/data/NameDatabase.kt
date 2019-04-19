package com.example.daoan.simplemvvm.name.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = arrayOf(Name::class), version = 1, exportSchema = false)
abstract class NameDatabase: RoomDatabase() {
    abstract fun nameDao(): NameDao
}