package com.example.daoan.simplemvvm.repository

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.example.daoan.simplemvvm.data.local.db.dao.UserDao
import com.example.daoan.simplemvvm.data.local.db.UserDatabase
import com.example.daoan.simplemvvm.data.model.User

interface UserRepository {
    fun getAllUsers(): LiveData<List<User>>
    suspend fun insertUser(user: User)
    suspend fun deleteUsers(vararg user: User)
}

class UserRepositoryImpl(private val database: UserDatabase) : UserRepository {
    private val userDao: UserDao = database.userDao()

    override fun getAllUsers(): LiveData<List<User>> {
        return userDao.getAllUsers()
    }

    @WorkerThread
    override suspend fun insertUser(user: User) {
        userDao.insertUser(user)
    }

    @WorkerThread
    override suspend fun deleteUsers(vararg user: User) {
        userDao.deleteUsers(*user)
    }

}