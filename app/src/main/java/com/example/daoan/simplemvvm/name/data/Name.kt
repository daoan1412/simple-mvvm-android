package com.example.daoan.simplemvvm.name.data

import androidx.annotation.Nullable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "name")
data class Name(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "nameid") val id: Int = 0,
    @ColumnInfo(name = "nametext") val name: String
)