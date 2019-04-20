package com.example.daoan.simplemvvm.data.model

import androidx.annotation.Nullable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "uid") val id: Int = 0,
    @ColumnInfo(name = "username") val username: String
)