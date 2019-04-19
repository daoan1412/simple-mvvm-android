package com.example.daoan.simplemvvm

import android.app.Application
import com.example.daoan.simplemvvm.di.applicationModule
import org.koin.android.ext.android.startKoin

class BaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin(this, listOf(applicationModule))
    }
}