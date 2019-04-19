package com.example.daoan.simplemvvm.name.data

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

interface NameRepository {
    fun getNames(): LiveData<List<Name>>
    suspend fun insertName(name: Name)
    suspend fun deleteNames(vararg name: Name)
}

class NameRepositoryImpl(private val database: NameDatabase) : NameRepository {
    private val nameDao: NameDao = database.nameDao()

    override fun getNames(): LiveData<List<Name>> {
        return nameDao.getNames()
    }

    @WorkerThread
    override suspend fun insertName(name: Name) {
        nameDao.insertName(name)
    }

    @WorkerThread
    override suspend fun deleteNames(vararg name: Name) {
        nameDao.deleteNames(*name)
    }

}