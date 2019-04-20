package com.example.daoan.simplemvvm.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.daoan.simplemvvm.data.local.db.dao.UserDao
import com.example.daoan.simplemvvm.data.model.User

@Database(entities = arrayOf(User::class), version = 1, exportSchema = false)
abstract class UserDatabase: RoomDatabase() {
    abstract fun userDao(): UserDao
}