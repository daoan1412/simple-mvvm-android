package com.example.daoan.simplemvvm.data.local.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.daoan.simplemvvm.data.model.User

@Dao
interface UserDao {
    @Query("SELECT * FROM users ORDER BY uid ASC")
    fun getAllUsers(): LiveData<List<User>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Query("DELETE FROM users WHERE uid =:uid")
    suspend fun deleteUser(uid: Int)
}