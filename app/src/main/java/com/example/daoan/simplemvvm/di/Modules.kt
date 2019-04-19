package com.example.daoan.simplemvvm.di

import androidx.room.Room
import com.example.daoan.simplemvvm.name.data.NameDatabase
import com.example.daoan.simplemvvm.name.data.NameRepository
import com.example.daoan.simplemvvm.name.data.NameRepositoryImpl
import com.example.daoan.simplemvvm.name.viewmodel.NameViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val applicationModule = module(override = true) {
    single { Room.databaseBuilder(androidContext(), NameDatabase::class.java, "name_db").build() }
    single<NameRepository> { NameRepositoryImpl(get()) }
    viewModel { NameViewModel(get()) }
}