package com.example.daoan.simplemvvm.di

import androidx.room.Room
import com.example.daoan.simplemvvm.data.local.db.UserDatabase
import com.example.daoan.simplemvvm.repository.UserRepository
import com.example.daoan.simplemvvm.repository.UserRepositoryImpl
import com.example.daoan.simplemvvm.viewmodel.UserViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val applicationModule = module(override = true) {
    single { Room.inMemoryDatabaseBuilder(androidContext(), UserDatabase::class.java).build() }
    single<UserRepository> {
        UserRepositoryImpl(get())
    }
    viewModel { UserViewModel(get()) }
}