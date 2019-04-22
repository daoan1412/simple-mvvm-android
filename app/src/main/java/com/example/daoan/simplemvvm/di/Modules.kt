package com.example.daoan.simplemvvm.di

import com.example.daoan.simplemvvm.repository.TaskRepository
import com.example.daoan.simplemvvm.repository.TaskRepositoryImpl
import com.example.daoan.simplemvvm.viewmodel.TaskViewModel
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val applicationModule = module(override = true) {
    single<TaskRepository> { TaskRepositoryImpl() }
    viewModel { TaskViewModel(get()) }
}