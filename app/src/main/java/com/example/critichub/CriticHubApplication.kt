package com.example.critichub

import android.app.Application
import com.example.critichub.di.AppContainer

class CriticHubApplication : Application() {

    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}
