package com.example.daoan.simplemvvm.name.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.daoan.simplemvvm.name.data.Name
import com.example.daoan.simplemvvm.name.data.NameRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class NameViewModel(private val repo: NameRepository) : ViewModel() {

    val allNames: LiveData<List<Name>> = repo.getNames()

    fun insertName(name: Name) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.insertName(name)
        }
    }


}