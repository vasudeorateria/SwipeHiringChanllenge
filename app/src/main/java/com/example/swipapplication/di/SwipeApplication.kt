package com.example.swipapplication.di

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class SwipeApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
//            androidContext(this@SwipeApplication)
            modules(appModule)
        }
    }
}