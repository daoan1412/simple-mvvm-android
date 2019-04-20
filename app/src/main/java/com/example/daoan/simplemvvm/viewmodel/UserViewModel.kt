package com.example.daoan.simplemvvm.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.daoan.simplemvvm.data.model.User
import com.example.daoan.simplemvvm.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class UserViewModel(private val repo: UserRepository) : ViewModel() {

    val allUsers: LiveData<List<User>> = repo.getAllUsers()

    fun insertUser(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.insertUser(user)
        }
    }

    fun deleteUser(uid: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteUser(uid)
        }
    }

}