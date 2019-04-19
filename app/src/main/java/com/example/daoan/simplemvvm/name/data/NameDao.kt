package com.example.daoan.simplemvvm.name.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface NameDao {
    @Query("SELECT * FROM name ORDER BY nameid ASC")
    fun getNames(): LiveData<List<Name>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertName(name: Name)

    @Delete
    suspend fun deleteNames(vararg name: Name)
}