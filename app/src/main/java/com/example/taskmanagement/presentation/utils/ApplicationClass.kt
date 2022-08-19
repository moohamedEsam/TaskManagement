package com.example.taskmanagement.presentation.utils

import android.app.Application
import android.os.StrictMode
import android.util.Log
import com.example.taskmanagement.presentation.koin.repository
import com.example.taskmanagement.presentation.koin.useCaseModules
import com.example.taskmanagement.presentation.koin.utils
import com.example.taskmanagement.presentation.koin.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.android.logger.AndroidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.core.logger.Logger
import org.koin.core.logger.MESSAGE

class ApplicationClass : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@ApplicationClass)
            modules(listOf(utils, viewModelModule, repository, useCaseModules))
            androidLogger()
        }
    }
}