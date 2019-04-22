package com.example.daoan.simplemvvm.app

import android.app.Application
import com.example.daoan.simplemvvm.di.applicationModule
import io.realm.Realm
import org.koin.android.ext.android.startKoin
import io.realm.RealmConfiguration


class BaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        setUpRealm()
        startKoin(this, listOf(applicationModule))
    }

    private fun setUpRealm() {
        Realm.init(this)
        val config = RealmConfiguration.Builder()
            .name("myrealm.realm")
            .schemaVersion(1)
//            .inMemory()
            .build()
        Realm.setDefaultConfiguration(config)
    }
}