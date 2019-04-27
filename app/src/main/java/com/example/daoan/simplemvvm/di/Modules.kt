package com.example.daoan.simplemvvm.di

import com.example.daoan.simplemvvm.data.model.MyObjectBox
import com.example.daoan.simplemvvm.repository.TaskRepository
import com.example.daoan.simplemvvm.repository.TaskRepositoryImpl
import com.example.daoan.simplemvvm.viewmodel.TaskViewModel
import io.objectbox.BoxStore
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val applicationModule = module(override = true) {
    single<BoxStore> {
        MyObjectBox.builder()
            .androidContext(
                androidContext()
                    .applicationContext
            )
            .build()
    }
    single<TaskRepository> { TaskRepositoryImpl(get()) }
    viewModel { TaskViewModel(get()) }
}