package com.example.daoan.simplemvvm.name.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.daoan.simplemvvm.name.data.Name
import com.example.daoan.simplemvvm.name.data.NameRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext


class NameViewModel(private val repo: NameRepository): ViewModel() {
    private var parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main
    private val scope = CoroutineScope(coroutineContext)

    val allNames: LiveData<List<Name>> = repo.getNames()

    fun insertName(name: Name) {
        scope.launch(Dispatchers.IO)  {
            repo.insertName(name)
        }
    }

    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }

}