package com.example.daoan.simplemvvm.base

import android.app.Application
import com.example.daoan.simplemvvm.di.applicationModule
import io.realm.Realm
import io.realm.RealmConfiguration
import org.koin.android.ext.android.startKoin


class BaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        setUpRealm()
        startKoin(this, listOf(applicationModule))
    }

    private fun setUpRealm() {
        Realm.init(this)
        val config = RealmConfiguration.Builder()
            .name("aea2lm.realm")
            .schemaVersion(2)
//            .inMemory()
            .build()
        Realm.setDefaultConfiguration(config)
    }
}