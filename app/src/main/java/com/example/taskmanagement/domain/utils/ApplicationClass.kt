package com.example.taskmanagement.domain.utils

import android.app.Application
import com.example.taskmanagement.presentation.koin.utils
import com.example.taskmanagement.presentation.koin.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class ApplicationClass : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@ApplicationClass)
            modules(listOf(utils, viewModelModule))
        }
    }
}